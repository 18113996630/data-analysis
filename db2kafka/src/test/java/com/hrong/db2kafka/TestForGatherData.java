package com.hrong.db2kafka;

import com.hrong.common.model.GatherData;
import com.hrong.db2kafka.service.ExtractDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hrong
 * @ClassName TestForGatherData
 * @Description
 * @Date 2019/6/30 10:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Db2kafkaApplication.class,TestForGatherData.class})
public class TestForGatherData {

	@Resource
	private ExtractDataService<GatherData> extractDataService;

	@Test
	public void test(){
		List<GatherData> gatherData = extractDataService.extractData();
		for (GatherData data : gatherData) {
			System.out.println(data);
		}
	}
}
