import 'react-native'
import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'

import { ActivityScreen } from '../../screens/ActivityScreen'

configure({ adapter: new Adapter() })

describe('ActivityScreen snapshot', () => {
  const activities = [
    {
      title: 'Fazer a cama',
      image: 'bed',
      photo: 'bedroom',
      color: '#7d84b2',
      time: {
        min: 0,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Lavar os dentes',
      image: 'toothbrush',
      color: '#0e79b2',
      time: {
        min: 1,
        max: 15,
        goal: 10
      }
    },
    {
      title: 'Vestir',
      image: 'socks',
      color: '#7fb800',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Tomar banho',
      image: 'shower',
      color: '#37c1f0',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Preparar a mochila',
      image: 'bag',
      color: '#e43f6f',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Calçar os sapatos',
      image: 'sneakers',
      color: '#4bb3fd',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Tomar o pequeno almoço',
      image: 'breakfast',
      color: '#ff7f11',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    },
    {
      title: 'Pentear cabelo',
      image: 'comb',
      color: '#b0db43',
      time: {
        min: 10,
        max: 120,
        goal: 60
      }
    }
  ]

  it('renders ActivityScreen correctly', () => {
    const wrapper = shallow(<ActivityScreen
      navigation={{ navigate: jest.fn(), popToTop: jest.fn(), replace: jest.fn() }}
      progressType={'bar'}
      activityFeedback={'vibration'}
      showTimer={false}
      currentActivity={0}
      activities={activities}
      activity={activities[0]}
      setActivityStatus={jest.fn()}
      nextActivity={jest.fn()}
      addStars={jest.fn()} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ showTimer: true })
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ progressType: 'clock' })
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ showTimer: true })
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.instance().pauseActivity()
    wrapper.instance().resumeActivity()
    wrapper.instance().backToMenu()
    wrapper.instance().cancelActivity()
    wrapper.instance().completeActivity()
    wrapper.instance().nextActivity()
    wrapper.setProps({ activity: {
      title: 'Fazer a cama',
      image: 'bed',
      photo: 'bedroom',
      color: '#7d84b2',
      time: {
        min: 0,
        max: 120,
        goal: 60
      },
      status: {
        reward: 0,
        completed: true,
        time: 120
      }
    } })
    wrapper.setState({ isPhoto: false })
    wrapper.instance().backToMenu()
    wrapper.instance().nextActivity()
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ activityFeedback: 'visual' })
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setState({ elapsedTime: 130 })
    wrapper.instance().completeActivity()
    wrapper.setState({ elapsedTime: 110 })
    wrapper.instance().completeActivity()
    wrapper.setState({ elapsedTime: 70 })
    wrapper.instance().completeActivity()
    wrapper.unmount()
  })
})
