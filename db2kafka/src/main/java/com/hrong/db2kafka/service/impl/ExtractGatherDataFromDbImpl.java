package com.hrong.db2kafka.service.impl;

import com.hrong.db2kafka.constant.BackEndConstant;
import com.hrong.db2kafka.constant.CacheConstant;
import com.hrong.db2kafka.dao.GatherDataMapper;
import com.hrong.common.model.GatherData;
import com.hrong.db2kafka.model.QueryParam;
import com.hrong.db2kafka.service.ExtractDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hrong
 * @ClassName ExtractGatherDataFromDbImpl
 * @Description 从数据库获取gather-data
 * @Date 2019/6/29 23:06
 **/
@Slf4j
@Service(value = "extractGatherDataFromDb")
public class ExtractGatherDataFromDbImpl implements ExtractDataService<GatherData> {

	@Value(value = "${query.offset:10}")
	private String offset;
	@Resource
	private GatherDataMapper gatherDataMapper;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 每次从数据库获取定量数据
	 * 起始位置为上一次获取的数据的最大的id值，数量为offset
	 * @return db中的数据
	 */
	@Override
	public List<GatherData> extractData() {
		Object redisId = redisTemplate.boundHashOps(CacheConstant.EXTRACT_KEY).get(CacheConstant.GATHER_DATA);
		if (redisId == null) {
			redisTemplate.boundHashOps(CacheConstant.EXTRACT_KEY).put(CacheConstant.GATHER_DATA, CacheConstant.INIT_ZERO);
			log.info("在redis中初始化数据提取进度");
			redisId = redisTemplate.boundHashOps(CacheConstant.EXTRACT_KEY).get(CacheConstant.GATHER_DATA);
		}
		int maxId = Integer.parseInt(String.valueOf(redisId));
		QueryParam queryParam = QueryParam.builder().start(maxId).offset(Integer.valueOf(offset)).build();
		return gatherDataMapper.selectByStartAndOffset(queryParam);
	}

}
