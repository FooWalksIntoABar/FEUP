import 'react-native'
import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'

import { RoutineBonusScreen } from '../../../screens/child/RoutineBonusScreen'
import { BonusModal } from '../../../components/RewardsModal/BonusModal'

configure({ adapter: new Adapter() })

describe('RoutineBonusScreen snapshot', () => {
  jest.useFakeTimers()
  it('renders RoutineBonusScreen correctly', () => {
    const wrapper = shallow(<RoutineBonusScreen
      navigation={{ navigate: jest.fn(), popToTop: jest.fn() }}
      level={1}
      xp={0}
      addStars={jest.fn()}Z
      playSounds />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.find(BonusModal).at(0).props().nextPress()
    wrapper.instance().componentWillUnmount()
    jest.advanceTimersByTime(1002)
    wrapper.instance()._confettiView = {
      startConfetti: jest.fn(),
      stopConfetti: jest.fn()
    }
    wrapper.instance()._confettiView2 = {
      startConfetti: jest.fn(),
      stopConfetti: jest.fn()
    }
    wrapper.instance().componentDidMount()
    wrapper.unmount()
  })
})
