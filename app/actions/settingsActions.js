import { settingTypes } from './actionTypes'

export function setSettings (settings) {
  return {
    type: settingTypes.setSettings,
    payload: settings
  }
}

export function changeVisualStyle (visualStyle) {
  return {
    type: settingTypes.changeVisualStyle,
    payload: visualStyle
  }
}

export function toggleActivityProgressType () {
  return {
    type: settingTypes.toggleActivityProgressType
  }
}

export function toggleActivityTimer () {
  return {
    type: settingTypes.toggleActivityTimer
  }
}

export function toggleActivityFeedback () {
  return {
    type: settingTypes.toggleActivityFeedback
  }
}

export function changeFeedbackFrequency () {
  return {
    type: settingTypes.changeFeedbackFrequency
  }
}

export function toggleRoutinePlayType () {
  return {
    type: settingTypes.toggleRoutinePlayType
  }
}
