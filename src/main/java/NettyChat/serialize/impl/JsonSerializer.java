package NettyChat.serialize.impl;

import NettyChat.serialize.Serializer;
import NettyChat.serialize.SerializerAlgorithm;
import com.alibaba.fastjson.JSON;

/**
 * Created by ShinChan on 2019/5/28.
 */
public class JsonSerializer implements Serializer{

    @Override
    public byte getSerializerAlgorithm(){
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object){
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes){
        return JSON.parseObject(bytes, clazz);
    }
}
