package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;

import com.qianlima.reptile.statistics.entity.OctopusLogInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 16:10 2020-03-08
 * @Modified By :
 */
@Component
@DS("octopus")
public interface OctopusMapper {
    /**
     * 查询数量和状态按状态分类
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count,a.state from octopus_monitor a,octopus_task b where a.verify_code <> 200 and a.task_id = b.task_id and a.verify_time >=#{startTime} and a.verify_time <#{endTime} and operate_id is not null and state > 0 group by state")
    List<Map<String,Object>> selectStatusCodeProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询未处理状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from octopus_monitor a,octopus_task b where a.verify_code <> 200 and a.task_id = b.task_id and a.verify_time >=#{startTime} and a.verify_time <#{endTime} and state = 0")
    String selectStatusCodeUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select `status`,count(*) as count from octopus_task_log WHERE create_time > #{startTime} and create_time < #{endTime} GROUP BY `status`")
    List<OctopusLogInfo> getLogInfos(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

}
