// Generated by gencpp from file simple_controller/custom_msg.msg
// DO NOT EDIT!


#ifndef SIMPLE_CONTROLLER_MESSAGE_CUSTOM_MSG_H
#define SIMPLE_CONTROLLER_MESSAGE_CUSTOM_MSG_H


#include <string>
#include <vector>
#include <map>

#include <ros/types.h>
#include <ros/serialization.h>
#include <ros/builtin_message_traits.h>
#include <ros/message_operations.h>

#include <std_msgs/Header.h>

namespace simple_controller
{
template <class ContainerAllocator>
struct custom_msg_
{
  typedef custom_msg_<ContainerAllocator> Type;

  custom_msg_()
    : header()
    , left_infrared(0.0)
    , right_infrared(0.0)
    , bottom_distance(0.0)
    , top_distance(0.0)  {
    }
  custom_msg_(const ContainerAllocator& _alloc)
    : header(_alloc)
    , left_infrared(0.0)
    , right_infrared(0.0)
    , bottom_distance(0.0)
    , top_distance(0.0)  {
  (void)_alloc;
    }



   typedef  ::std_msgs::Header_<ContainerAllocator>  _header_type;
  _header_type header;

   typedef double _left_infrared_type;
  _left_infrared_type left_infrared;

   typedef double _right_infrared_type;
  _right_infrared_type right_infrared;

   typedef double _bottom_distance_type;
  _bottom_distance_type bottom_distance;

   typedef double _top_distance_type;
  _top_distance_type top_distance;





  typedef boost::shared_ptr< ::simple_controller::custom_msg_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::simple_controller::custom_msg_<ContainerAllocator> const> ConstPtr;

}; // struct custom_msg_

typedef ::simple_controller::custom_msg_<std::allocator<void> > custom_msg;

typedef boost::shared_ptr< ::simple_controller::custom_msg > custom_msgPtr;
typedef boost::shared_ptr< ::simple_controller::custom_msg const> custom_msgConstPtr;

// constants requiring out of line definition



template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const ::simple_controller::custom_msg_<ContainerAllocator> & v)
{
ros::message_operations::Printer< ::simple_controller::custom_msg_<ContainerAllocator> >::stream(s, "", v);
return s;
}

} // namespace simple_controller

namespace ros
{
namespace message_traits
{



// BOOLTRAITS {'IsFixedSize': False, 'IsMessage': True, 'HasHeader': True}
// {'simple_controller': ['/home/arthur/catkin_ws/src/simple_controller/msg'], 'std_msgs': ['/opt/ros/kinetic/share/std_msgs/cmake/../msg']}

// !!!!!!!!!!! ['__class__', '__delattr__', '__dict__', '__doc__', '__eq__', '__format__', '__getattribute__', '__hash__', '__init__', '__module__', '__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_parsed_fields', 'constants', 'fields', 'full_name', 'has_header', 'header_present', 'names', 'package', 'parsed_fields', 'short_name', 'text', 'types']




template <class ContainerAllocator>
struct IsFixedSize< ::simple_controller::custom_msg_<ContainerAllocator> >
  : FalseType
  { };

template <class ContainerAllocator>
struct IsFixedSize< ::simple_controller::custom_msg_<ContainerAllocator> const>
  : FalseType
  { };

template <class ContainerAllocator>
struct IsMessage< ::simple_controller::custom_msg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::simple_controller::custom_msg_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::simple_controller::custom_msg_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::simple_controller::custom_msg_<ContainerAllocator> const>
  : TrueType
  { };


template<class ContainerAllocator>
struct MD5Sum< ::simple_controller::custom_msg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "cbb65c1d0fa6478c98e5b5813e063afb";
  }

  static const char* value(const ::simple_controller::custom_msg_<ContainerAllocator>&) { return value(); }
  static const uint64_t static_value1 = 0xcbb65c1d0fa6478cULL;
  static const uint64_t static_value2 = 0x98e5b5813e063afbULL;
};

template<class ContainerAllocator>
struct DataType< ::simple_controller::custom_msg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "simple_controller/custom_msg";
  }

  static const char* value(const ::simple_controller::custom_msg_<ContainerAllocator>&) { return value(); }
};

template<class ContainerAllocator>
struct Definition< ::simple_controller::custom_msg_<ContainerAllocator> >
{
  static const char* value()
  {
    return "Header header\n\
float64 left_infrared\n\
float64 right_infrared\n\
float64 bottom_distance\n\
float64 top_distance\n\
\n\
\n\
================================================================================\n\
MSG: std_msgs/Header\n\
# Standard metadata for higher-level stamped data types.\n\
# This is generally used to communicate timestamped data \n\
# in a particular coordinate frame.\n\
# \n\
# sequence ID: consecutively increasing ID \n\
uint32 seq\n\
#Two-integer timestamp that is expressed as:\n\
# * stamp.sec: seconds (stamp_secs) since epoch (in Python the variable is called 'secs')\n\
# * stamp.nsec: nanoseconds since stamp_secs (in Python the variable is called 'nsecs')\n\
# time-handling sugar is provided by the client library\n\
time stamp\n\
#Frame this data is associated with\n\
# 0: no frame\n\
# 1: global frame\n\
string frame_id\n\
";
  }

  static const char* value(const ::simple_controller::custom_msg_<ContainerAllocator>&) { return value(); }
};

} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

  template<class ContainerAllocator> struct Serializer< ::simple_controller::custom_msg_<ContainerAllocator> >
  {
    template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
    {
      stream.next(m.header);
      stream.next(m.left_infrared);
      stream.next(m.right_infrared);
      stream.next(m.bottom_distance);
      stream.next(m.top_distance);
    }

    ROS_DECLARE_ALLINONE_SERIALIZER
  }; // struct custom_msg_

} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::simple_controller::custom_msg_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const ::simple_controller::custom_msg_<ContainerAllocator>& v)
  {
    s << indent << "header: ";
    s << std::endl;
    Printer< ::std_msgs::Header_<ContainerAllocator> >::stream(s, indent + "  ", v.header);
    s << indent << "left_infrared: ";
    Printer<double>::stream(s, indent + "  ", v.left_infrared);
    s << indent << "right_infrared: ";
    Printer<double>::stream(s, indent + "  ", v.right_infrared);
    s << indent << "bottom_distance: ";
    Printer<double>::stream(s, indent + "  ", v.bottom_distance);
    s << indent << "top_distance: ";
    Printer<double>::stream(s, indent + "  ", v.top_distance);
  }
};

} // namespace message_operations
} // namespace ros

#endif // SIMPLE_CONTROLLER_MESSAGE_CUSTOM_MSG_H
