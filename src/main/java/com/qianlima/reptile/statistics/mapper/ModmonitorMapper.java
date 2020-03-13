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
 * @Date : Created in 16:08 2020-03-08
 * @Modified By :
 */
@Component
@DS("modmonitor")
public interface ModmonitorMapper {

    @Select("select count(1) as count,state from fail_tmplt where type =  1  and valid_time is not null  and valid_state <> 200 and  create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectService(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from fail_tmplt where type =  3  and valid_state <> 200 and create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectParsing(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from fail_tmplt where type = 5 and list_size = 0 and create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectValue(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from fail_tmplt where type =  6  and valid_state <> 200 and valid_time is not null and create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectEarlyService(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from fail_tmplt where type =  7 and valid_state <> 200 and create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectEarlyParsing(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from fail_tmplt where type =  8 and valid_state <> 200 and list_size = 0 and create_time >= #{startTime} and create_time <= #{endTime} group by state")
    List<Map<String, Object>> selectEarlyValue(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count,state from artificial_editor_monitor  where verify_code <> 200 and verify_time >=#{startTime} and verify_time <#{endTime} group by state")
    List<Map<String, Object>> selectHumanEditors(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) as count, state from modmonitor.messy_tmplt where create_time > #{startTime} and create_time<#{endTime} group by state")
    List<Map<String, Object>> selectMessyCode(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select  count(distinct(pot_name))   from  fail_tmplt  where  type = 1  and  create_time  >  #{startTime}  and  create_time  < #{endTime}   and  valid_state  <>  200")
    String selectPotTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select  count(distinct(pot_name))  from  fail_tmplt  where  type = 1 and    create_time  >   #{startTime}  and  create_time  < #{endTime}   and  valid_state  <>  200  and  state  =  0")
    String selectPotUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select  count(distinct(pot_name))   from  fail_tmplt  where  type  =  1  and    create_time  >  #{startTime}   and  create_time  <  #{endTime}  and  valid_state  <>  200  and  state  >  0")
    String selectPotProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
