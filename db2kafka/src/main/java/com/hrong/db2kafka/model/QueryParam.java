package com.hrong.db2kafka.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author hrong
 * @ClassName QueryParam
 * @Description
 * @Date 2019/6/30 10:45
 **/
@Data
@Builder
public class QueryParam {
	private int start;
	private int offset;
}
