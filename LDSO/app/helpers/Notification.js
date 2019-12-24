import { Permissions, Notifications } from 'expo'
import EnvVars from '../constants/EnviromentVars'

export async function registerForPushNotificationsAsync () {
  const { status: existingStatus } = await Permissions.getAsync(Permissions.NOTIFICATIONS)
  let finalStatus = existingStatus

  if (existingStatus !== 'granted') {
    const { status } = await Permissions.askAsync(Permissions.NOTIFICATIONS)
    finalStatus = status
  }

  if (finalStatus !== 'granted') {
    return
  }

  let token = await Notifications.getExpoPushTokenAsync()
  console.log(token)

  let requestInit = {
    method: 'POST',
    headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
    body: JSON.stringify({ token: { value: token }, user: { username: 'Brent' } })
  }

  return fetch(EnvVars.apiUrl + 'routine_manager/push-token/', requestInit)
}