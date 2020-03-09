package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
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

    @Select("select count(1) as count,a.state from octopus_monitor a,octopus_task b where a.verify_code <> 200 and a.task_id = b.task_id and a.verify_time >=#{startTime} and a.verify_time <#{endTime} group by state")
    List<Map<String,Object>> selectStatusCode(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
