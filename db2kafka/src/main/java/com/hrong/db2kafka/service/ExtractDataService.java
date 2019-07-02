package com.hrong.db2kafka.service;

import java.util.List;

/**
 * @Author hrong
 * @ClassName ExtractDataService
 * @Description 获取待推送到kafka的数据接口
 * @Date 2019/6/29 23:01
 **/
public interface ExtractDataService<T> {
	/**
	 * 提取数据
	 * @return result data
	 */
	List<T> extractData();
}
