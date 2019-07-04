package com.hrong.analysis.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

import java.io.IOException;

/**
 * @Author hrong
 * 自定义的kafka反序列化器
 **/
@Slf4j
public class UserDefinedKafkaDeserializer<T> implements DeserializationSchema<T> {
	private Class<T> clazz;

	public UserDefinedKafkaDeserializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 将接受到的数据反序列化为需要的对象格式
	 * @param message 二进制数据
	 * @return 反序列化后的对象
	 * @throws IOException 无法序列化时抛出
	 */
	@Override
	public T deserialize(byte[] message) throws IOException {
		T res = null;
		try {
			res = JSON.parseObject(new String(message), clazz);
		} catch (Exception e) {
			log.error("数据格式错误：{} 错误数据：{}", e.getMessage(), new String(message));
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public boolean isEndOfStream(T nextElement) {
		return false;
	}

	@Override
	public TypeInformation<T> getProducedType() {
		return TypeExtractor.getForClass(clazz);
	}
}
