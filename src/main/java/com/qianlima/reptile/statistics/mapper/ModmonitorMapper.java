package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
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

    /**
     * 查询全部未处理非重复模版数据
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select tmplt,state,update_time as updateTime from fail_tmplt where type =  1  and valid_time is not null  and valid_state <> 200 and  create_time >= #{startTime} and create_time <= #{endTime} and state = 0 " +
            "union select tmplt,state,update_time as updateTime from fail_tmplt where type =  3  and valid_state <> 200 and create_time >= #{startTime} and create_time <= #{endTime} and state = 0 " +
            "union select tmplt,state,update_time as updateTime from fail_tmplt where type = 5 and list_size = 0 and create_time >= #{startTime} and create_time <= #{endTime} and state = 0 ")
    List<FaultTmpltDo> selectTenderUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询招标模版全部人工处理数据
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select tmplt,state,update_time as updateTime from fail_tmplt where type =  1  and valid_time is not null  and valid_state <> 200 and  create_time >=  #{startTime} and create_time <= #{endTime} and state > -2 and operate_id is not null " +
            "union select tmplt,state,update_time as updateTime from fail_tmplt where type =  3  and valid_state <> 200 and create_time >=  #{startTime} and create_time <= #{endTime} and state > -2 and operate_id is not null " +
            "union select tmplt,state,update_time as updateTime from fail_tmplt where type = 5 and list_size = 0 and create_time >=  #{startTime} and create_time <= #{endTime} and state > -2 and operate_id is not null")
    List<FaultTmpltDo> selectTenderProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期服务器故障人工已处理各个状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count,state from fail_tmplt where type =  6  and valid_state <> 200 and valid_time is not null and create_time >= #{startTime} and create_time <= #{endTime} and operate_id is not null and state > 0 group by state ")
    List<Map<String, Object>> selectEarlyServiceProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期解析故障人工已处理各个状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count,state from fail_tmplt where type =  7 and valid_state <> 200 and create_time >= #{startTime} and create_time <= #{endTime} and operate_id is not null and state > 0 group by state")
    List<Map<String, Object>> selectEarlyParsingProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期解析值故障人工已处理各个状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count,state from fail_tmplt where type =  8 and valid_state <> 200 and list_size = 0 and create_time >= #{startTime} and create_time <= #{endTime} and operate_id is not null and state > 0 group by state")
    List<Map<String, Object>> selectEarlyValueProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期服务器故障未处理数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from fail_tmplt where type =  6  and valid_state <> 200 and valid_time is not null and create_time >= #{startTime} and create_time <= #{endTime} and state = 0")
    String selectEarlyServiceUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期解析故障未处理数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from fail_tmplt where type =  7 and valid_state <> 200 and create_time >= #{startTime} and create_time <= #{endTime} and state = 0")
    String selectEarlyParsingUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前期解析值故障未处理数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from fail_tmplt where type =  8 and valid_state <> 200 and list_size = 0 and create_time >= #{startTime} and create_time <= #{endTime} and state = 0")
    String selectEarlyValueUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询人工编辑人工已处理各个状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count,state from artificial_editor_monitor  where verify_code <> 200 and verify_time >=#{startTime} and verify_time <#{endTime} and operate_id is not null and state > 0 group by state")
    List<Map<String, Object>> selectHumanEditorsProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询招标乱码人工已处理各个状态数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) as count, state from modmonitor.messy_tmplt where create_time > #{startTime} and create_time<#{endTime} and operater_id is not null and state > 0 group by state")
    List<Map<String, Object>> selectMessyCodeProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询人工编辑未处理数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from artificial_editor_monitor  where verify_code <> 200 and verify_time >=#{startTime} and verify_time <#{endTime} and state = 0")
    String selectHumanEditorsUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询招标乱码未处理数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from modmonitor.messy_tmplt where create_time > #{startTime} and create_time<#{endTime} and state = 0")
    String selectMessyCodeUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询未处理POT量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select  count(distinct(pot_name))  from  fail_tmplt  where  type in (1,3,5) and create_time >= #{startTime} and create_time <= #{endTime}   and  valid_state  <>  200  and  state  =  0")
    String selectPotUntreated(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 已处理POT量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select  count(distinct(pot_name))   from  fail_tmplt  where  type in (1,3,5)  and create_time  > #{startTime} and create_time <= #{endTime} and  valid_state  <>  200  and  state  >  0  and operate_id is not null")
    String selectPotProcessed(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询crawlconfig表中各个分类下id列表
     * @Param [dimOrgUrl]
     * @return java.util.List<java.lang.String>
     **/
    @Select("SELECT id from rawdatas.crawlconfig where orgurl LIKE CONCAT('%',#{dimOrgUrl},'%')  limit #{start},1000 ")
    List<String> selectCrawlconfigByOrgUrl(@Param("dimOrgUrl") String dimOrgUrl,@Param("start")Integer start);
    @Select("SELECT id from rawdatas.crawlconfig where orgurl not like '%bridge/octopus_list%' and orgurl not like '%column/datalist_n.jspt%' and orgurl not like '%bridge/wechat_list%' and orgurl not like '%bridge/bidding_list%' and orgurl not like '%bridge/peer_list%' limit #{start},1000")
    List<String> selectCrawlconfigMainCraw(@Param("start") Integer start);

    @Select({
            "<script>" +
            "select COUNT(1) from rawdatas.bidding_raw where ae_template in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            " and intime > #{startTime} and intime &lt; #{endTime} "+
            "</script>"
    })
    Integer selectBiddingCountsByIds(@Param("ids") List<String> ids, @Param("startTime") Integer startTime, @Param("endTime") Integer endTime);


}
