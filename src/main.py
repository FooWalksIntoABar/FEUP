#!/usr/bin/env python3

from navigation.utils.position import *
from robot import *

import rospy

def setup():
    # Tell rospy the name of your node -- until rospy has this information, it cannot start communicating with the
    # ROS Master. In this case, your node will take on the name 'main'
    rospy.init_node('main')
    r = Robot(Position(5, 5), rows=5)
    r.set_target(Position(2, 2))
 

   


if __name__ == '__main__':
    setup()
