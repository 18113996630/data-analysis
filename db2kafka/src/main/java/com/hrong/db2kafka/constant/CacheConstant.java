package com.hrong.db2kafka.constant;

import scala.Int;

/**
 * @Author hrong
 * @ClassName CacheConstant
 * @Description
 * @Date 2019/6/30 15:54
 **/
public class CacheConstant {
	/**
	 * max_id为key的hash表来记录每个表的读取进度
	 */
	public static final String EXTRACT_KEY = "extract_max_id";
	/**
	 * gather_data表
	 */
	public static final String GATHER_DATA = "gather_data";
	/**
	 * 初始化id值
	 */
	public static final Long INIT_ZERO = 0L;
	/**
	 * 总共推送的数据量
	 */
	public static final String PUSH_AMOUNTS = "push_total_amounts";
}
