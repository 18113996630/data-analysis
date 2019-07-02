package com.hrong.db2kafka.job;

import com.alibaba.fastjson.JSONArray;
import com.hrong.db2kafka.constant.CacheConstant;
import com.hrong.db2kafka.kafka.Producer;
import com.hrong.common.model.GatherData;
import com.hrong.db2kafka.service.ExtractDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import scala.Int;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author hrong
 * @ClassName ExtractDb2KafkaJob
 * @Description 从db中获取数据，将数据写入kafka
 * @Date 2019/6/29 22:44
 **/
@Component
@Slf4j
public class ExtractDb2KafkaJob implements CommandLineRunner {
	@Resource
	private ExtractDataService<GatherData> extractDataServiceImpl;
	@Resource
	private Producer producer;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource(name = "stringLongRedisTemplate")
	private RedisTemplate<String,String> stringLongRedisTemplate;


	@Override
	public void run(String... args) {
		redisTemplate.boundHashOps(CacheConstant.EXTRACT_KEY).put(CacheConstant.GATHER_DATA, CacheConstant.INIT_ZERO);
		redisTemplate.delete(CacheConstant.PUSH_AMOUNTS);
		log.warn("测试时每次从id为1的位置开始读取，正式环境请删除该代码：com.hrong.db2kafka.job.ExtractDb2KafkaJob.run前两行");
		while (true){
			job();
			Object amounts = stringLongRedisTemplate.boundValueOps(CacheConstant.PUSH_AMOUNTS).get();
			log.info("当前推送数据量：{}", amounts);
			if (Integer.valueOf(Objects.requireNonNull(amounts).toString()) == 10) {
				break;
			}
		}
	}

	private void job() {
		List<GatherData> data = extractDataServiceImpl.extractData();
		// 获取已推送的数据量
		Object beforeAmounts = stringLongRedisTemplate.boundValueOps(CacheConstant.PUSH_AMOUNTS).get();
		if (beforeAmounts == null) {
			// 将redis中已推送的数据初始化为0
			log.info("将redis中已推送的数据初始化为0");
			stringLongRedisTemplate.boundValueOps(CacheConstant.PUSH_AMOUNTS).set(String.valueOf(CacheConstant.INIT_ZERO));
			beforeAmounts = 0;
		}
		log.info("已推送数据量:{}，此次从数据库提取数据共{}条，开始将数据写入kafka", beforeAmounts, data.size());

		long start = System.currentTimeMillis();
		producer.sendMessage(JSONArray.toJSONString(data));
		long end = System.currentTimeMillis();
		log.info("花费时间：{}",(end-start));

		// 在redis中更新成功推送的数据量
		stringLongRedisTemplate.boundValueOps(CacheConstant.PUSH_AMOUNTS).increment(data.size());
		log.info("此次成功推送{}条数据", data.size());
		Integer nowMaxId = data.get(data.size() - 1).getId();
		redisTemplate.boundHashOps(CacheConstant.EXTRACT_KEY).put(CacheConstant.GATHER_DATA, nowMaxId);
		log.info("更新redis中表：{}已提取数据最大id值更新为:{}","gather_data", nowMaxId);
	}
}
