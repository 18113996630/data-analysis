package com.hrong.db2kafka.dao;

import com.hrong.common.model.GatherData;
import com.hrong.db2kafka.model.QueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangrong
 */
@Mapper
@Repository
public interface GatherDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GatherData record);

    int insertSelective(GatherData record);

    GatherData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatherData record);

    int updateByPrimaryKey(GatherData record);

    List<GatherData> selectByStartAndOffset(QueryParam queryParam);
}