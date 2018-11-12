import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { setActivityStatus, nextActivity } from '../actions/gameActions'
import { Image, Text, View, StatusBar } from 'react-native'
import { handleAndroidBackButton, removeAndroidBackButtonHandler } from '../helpers/AndroidBackButton'

import { ProgressBar } from '../components/Activity/ProgressBar'
import { ProgressClock } from '../components/Activity/ProgressClock'
import { CompleteButton } from '../components/Activity/CompleteButton'
import { PauseButton } from '../components/Activity/PauseButton'
import { CancelButton } from '../components/Activity/CancelButton'
import { RewardsModal } from '../components/RewardsModal/RewardsModal'
import Images from '../assets/images/images'

import styles from '../styles/Activity.style'

class ActivityScreen extends Component {
  constructor (props) {
    super(props)

    this.activity = this.props.activities[this.props.currentActivity]

    this.state = {
      elapsedTime: 0,
      progressType: '',
      isPhoto: this.activity.photo !== undefined,
      updateRate: 100, // ms
      isPaused: false,
      isCompleted: false,
      isCompletable: false
    }

    this.pauseActivity = this.pauseActivity.bind(this)
    this.cancelActivity = this.cancelActivity.bind(this)
    this.completeActivity = this.completeActivity.bind(this)
    this.resumeActivity = this.resumeActivity.bind(this)
    this.nextActivity = this.nextActivity.bind(this)
    this.backToMenu = this.backToMenu.bind(this)
  }

  static navigationOptions = {
    header: null
  }

  componentDidMount () {
    handleAndroidBackButton(this.cancelActivity)

    this.interval = setInterval(() => {
      if (this.state.isPaused) return
      if (this.state.elapsedTime >= this.activity.time.max) this.completeActivity()
      if (this.state.elapsedTime >= this.activity.time.min) {
        this.state.isCompletable = true
      }

      this.setState(() => {
        return { elapsedTime: this.state.elapsedTime + this.state.updateRate / 1000 }
      })
    }, this.state.updateRate)
  }

  componentWillUnmount () {
    clearInterval(this.interval)
    removeAndroidBackButtonHandler()
  }

  pauseActivity () {
    this.setState(() => ({ isPaused: true }))
  }

  cancelActivity () {
    this.props.navigation.popToTop()
  }

  completeActivity () {
    clearInterval(this.interval)
    const status = {
      completed: true,
      reward: this.state.elapsedTime > this.activity.time.max ? 0
        : this.state.elapsedTime < this.activity.time.goal ? 3
          : this.state.elapsedTime < this.activity.time.goal + (this.activity.time.max - this.activity.time.goal) / 2 ? 2 : 1,
      time: parseInt(this.state.elapsedTime)
    }
    this.props.setActivityStatus(this.activity, status)
    this.setState(() => { return { isCompleted: true } })
  }

  nextActivity () {
    this.props.nextActivity()
    this.props.navigation.replace('Activity')
  }

  backToMenu () {
    this.props.navigation.popToTop()
  }

  resumeActivity () {
    this.setState(() => ({ isPaused: false }))
  }

  render () {
    return (
      <View style={[{ backgroundColor: this.activity.color }, styles.activityScreen]} >
        <StatusBar hidden />
        <Image
          style={this.state.isPhoto ? styles.photo : styles.image}
          resizeMode={this.state.isPhoto ? 'cover' : 'center'}
          source={Images[this.state.isPhoto ? this.activity.photo : this.activity.image]} />
        <View style={styles.titleContainer}>
          <Text style={this.state.isPhoto ? styles.photoTitle : styles.title}>{this.activity.title}</Text>
        </View>
        {this.props.progressType === 'clock' && !this.state.isCompleted && <ProgressClock showTimer={this.props.showTimer} elapsedTime={this.state.elapsedTime} activityTimes={this.activity.time} isPaused={this.state.isPaused} />}
        {!this.state.isCompleted && <View style={styles.buttonContainer}>
          {this.props.progressType === 'bar' && <ProgressBar showTimer={this.props.showTimer} elapsedTime={this.state.elapsedTime} activityTimes={this.activity.time} isPaused={this.state.isPaused} />}
          <CancelButton style={styles.smallButton} cancelActivity={this.cancelActivity} />
          <PauseButton style={styles.smallButton} pauseActivity={this.pauseActivity} resumeActivity={this.resumeActivity} isPaused={this.state.isPaused} />
          <CompleteButton style={styles.largeButton} isCompletable={this.state.isCompletable} completeActivity={this.completeActivity} />
        </View>}
        <RewardsModal
          currentActivity={this.props.currentActivity}
          activities={this.props.activities}
          nextPress={this.nextActivity}
          backPress={this.backToMenu} />
      </View>
    )
  }
}

export default connect(
  state => ({
    progressType: state.settings.activityProgressType,
    showTimer: state.settings.activityShowTimer,
    activity: state.game.routines[state.game.currentRoutine].activities[state.game.currentActivity],
    activities: state.game.routines[state.game.currentRoutine].activities,
    currentActivity: state.game.currentActivity
  }),
  dispatch => ({
    setActivityStatus: (activity, status) => dispatch(setActivityStatus(activity, status)),
    nextActivity: () => dispatch(nextActivity())
  })
)(ActivityScreen)

ActivityScreen.propTypes = {
  navigation: PropTypes.object.isRequired,
  progressType: PropTypes.string.isRequired,
  showTimer: PropTypes.bool.isRequired,
  currentActivity: PropTypes.number.isRequired,
  activities: PropTypes.array.isRequired,
  setActivityStatus: PropTypes.func.isRequired,
  nextActivity: PropTypes.func.isRequired
}
