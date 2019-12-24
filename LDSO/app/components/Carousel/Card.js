import React from 'react'
import { View, Text, Image, TouchableWithoutFeedback } from 'react-native'
import PropTypes from 'prop-types'
import { getSource } from '../../helpers/GetSource'
import CardButton from './CardButton'

import { getCardStyle } from '../../styles/CardCarousel.style'

export default class Card extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      isPhoto: this.props.item.photo !== null,
      hasButton: this.props.onButtonPress !== undefined
    }
  }

  onPress = () => {
    this.props.onPress(this.props.item)
  }

  onButtonPress = () => {
    this.props.onButtonPress(this.props.item)
  }

  render () {
    const cardStyle = getCardStyle(this.props.item.color)
    return (
      <View style={cardStyle.cardContainer}>
        <TouchableWithoutFeedback
          onPress={this.onPress}>
          <View style={cardStyle.card}>
            <Image
              source={getSource(this.props.item)}
              resizeMode={this.state.isPhoto ? 'cover' : 'center'}
              style={this.state.isPhoto ? cardStyle.cardPhoto : this.props.isRoutine ? cardStyle.cardRoutineImage : cardStyle.cardActivityImage} />
            <Text style={this.state.isPhoto ? cardStyle.photoCardTitle : cardStyle.cardTitle}> { this.props.item.title } </Text>
            {this.state.hasButton && <CardButton cardStyle={cardStyle} onPress={this.onButtonPress} />}
          </View>
        </TouchableWithoutFeedback>
      </View>
    )
  }
}

Card.propTypes = {
  onPress: PropTypes.func.isRequired,
  onButtonPress: PropTypes.func,
  item: PropTypes.object.isRequired,
  isRoutine: PropTypes.bool
}