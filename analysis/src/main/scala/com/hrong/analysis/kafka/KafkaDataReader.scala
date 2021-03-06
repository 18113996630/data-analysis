package com.hrong.analysis.kafka

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONArray}
import com.hrong.common.model.GatherData
import javax.annotation.Resource
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.util.Collector
import org.springframework.data.redis.core.RedisTemplate

/**
  * 读取kafka数据并进行指标计算
  */
object KafkaDataReader {

  var redisTemplate = new RedisTemplate[String, String]()

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //开启五秒钟一次的checkpoint
    env.enableCheckpointing(5000L)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "n151:9092,n152:9092,n153:9092")
    properties.setProperty("zookeeper.connect", "n151:2181,n152:2181,n153:2181")
    properties.setProperty("group.id", "g1")
    //    val kafkaDeserializer = new UserDefinedKafkaDeserializer[String]()
    val kafkaDeserializer = new SimpleStringSchema()
    // 创建kafka消费者
    val consumer = new FlinkKafkaConsumer011[String]("test", kafkaDeserializer, properties)
    consumer.setStartFromLatest()

    val stream: DataStreamSource[String] = env.addSource(consumer)
    //暂时还没想好需求
    env.execute(this.getClass.getName)
  }
}
