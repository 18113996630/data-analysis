package com.hrong.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Date;

/**
 * @author huangrong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GatherData {


	private Integer id;
	private String guid;
	private String title;
	private String content;
	private Integer defineId;
	private Date gatherTime;

	public void setContent(String content) {
		this.content = Base64.getEncoder().encodeToString(content.getBytes());
	}
}