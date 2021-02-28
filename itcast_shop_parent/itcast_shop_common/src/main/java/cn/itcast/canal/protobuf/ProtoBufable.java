package cn.itcast.canal.protobuf;


//定义一个protobuf序列化接口，返回二进制字节码
//所有能够使用protobuf的bean都需要继承这个接口


public interface ProtoBufable {
    byte[] toBytes();

}
