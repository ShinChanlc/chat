package NettyChat.serialize;

import NettyChat.serialize.impl.JsonSerializer;

/**
 * Created by ShinChan on 2019/5/28.
 */
public interface Serializer {

    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JsonSerializer();

    byte getSerializerAlgorithm();//获取具体的序列化算法标识

    byte[] serialize(Object object);//将java对象转换成字节数组

    <T> T deserialize(Class<T> clazz, byte[] bytes);//将字节数组转换成某种类型的java对象
}
