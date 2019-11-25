#!/usr/bin/env python
#################################################################################
# Copyright 2018 ROBOTIS CO., LTD.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#################################################################################

# Authors: Gilbert #

import rospy
from geometry_msgs.msg import Twist
from turtlebot3_msgs.msg import SensorState

class Infrared():
    def __init__(self):
        self.cmd_pub = rospy.Publisher('cmd_vel', Twist, queue_size = 1)
        self.infrared_sub = rospy.Subscriber('infrared', SensorState, self.handleInfrared, queue_size = 1)
        self.infrared()

    def handleInfrared(self, sensor):
        print("Value: ", sensor.data)
        twist = Twist()
        if sensor.data < 1000:
            linear_vel = 0
        else:
            linear_vel = 0.05

        twist.linear.x = linear_vel
        self.cmd_pub.publish(twist)

    def cliff(self):
        rate = rospy.Rate(10)
        while not rospy.is_shutdown():
            rate.sleep()

def start():
    try:
        infrared = Infrared()
    except rospy.ROSInterruptException:
        pass

