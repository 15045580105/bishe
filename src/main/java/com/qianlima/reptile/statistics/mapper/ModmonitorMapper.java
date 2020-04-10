package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.*;
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
     * 分页查询crawlconfig表中各个分类下id列表
     * @Param [dimOrgUrl]
     * @return java.util.List<java.lang.String>
     **/
    @Select("SELECT id from rawdatas.crawlconfig where orgurl LIKE CONCAT('%',#{dimOrgUrl},'%')  limit #{start},1000 ")
    List<String> selectCrawlconfigByOrgUrl(@Param("dimOrgUrl") String dimOrgUrl,@Param("start")Integer start);

    /**
     * 分页查询crawconfig表中主爬虫数据id列表
     * @param start
     * @return
     */
    @Select("SELECT id from rawdatas.crawlconfig where orgurl not like '%bridge/octopus_list%' and orgurl not like '%column/datalist_n.jspt%' and orgurl not like '%bridge/bidding_list%' and orgurl not like '%bridge/peer_list%' limit #{start},1000")
    List<String> selectCrawlconfigMainCraw(@Param("start") Integer start);


    /**
     * 查询故障模版数量
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select count(1) from fail_tmplt  where valid_state <> 200 and update_time >=#{startTime} and update_time <#{endTime}")
    long selectFailTempltCount(@Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 查询故障模板 ，去重
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select DISTINCT(tmplt) from fail_tmplt  where type = 1 and valid_state <> 200 and update_time >=#{startTime} and update_time <=#{endTime}")
    List<FaultTmpltDo> selectFailTemplt(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询故障模板
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select DISTINCT(tmplt) from fail_tmplt  where type = 1 and valid_state <> 200 and update_time >=#{startTime} and update_time <=#{endTime} and tmplt = #{tmplt}")
    List<FaultTmpltDo> selectFailTempltByTmplt(@Param("tmplt") String tmplt,@Param("startTime") String startTime, @Param("endTime") String endTime);


    @Select("SELECT ap.id,ap.name as name,ap.domain,pa.name as area, ap.creater ,ap.intime from rawdatas.ae_pot ap LEFT JOIN rawdatas.phpcms_area pa on ap.area = pa.areaid where ap.domain = #{domain}")
    PotDetail selectPotDetailByPotName(String domain);

    @Select("select * from rawdatas.ccbeizhu where cid = #{id}")
    List<PotNote> selectPotNoteByPotId(Integer id);

    @Select("SELECT id, potName, isxm, cat, state , createtime, updateTime, collect_strategy FROM rawdatas.crawlconfig WHERE potName = #{potName} limit #{page},#{size}")
    List<TemplateInfo> selectTemplateInfosByName(@Param("potName") String potName, @Param("page") Integer page, @Param("size") Integer size);

    @Select("SELECT id FROM rawdatas.crawlconfig WHERE potName = #{potName}")
    List<String> selectTemplateIdByName(String potName);


    @Select("SELECT count(1) FROM rawdatas.crawlconfig WHERE potName = #{potName}")
    Integer selectTemplateInfosCountByName(String potName);

    @Select(
            "<script>" +
            "SELECT " +
            "template.id, " +
            "template.potName, " +
            "potnote.content, " +
            "template.isxm, " +
            "template.cat, " +
            "template.state, " +
            "template.createtime, " +
            "template.updateTime, " +
            "template.collect_strategy " +
            "from rawdatas.crawlconfig template " +
            "LEFT JOIN rawdatas.ccbeizhu potnote " +
            "ON template.id = potnote.cid " +
            "<where >" +
            "<if test = 'id != null'>" +
            "and template.id = #{id} " +
            "</if>"+
            "<if test = 'state != null'>" +
            "and template.state = #{state} " +
            "</if>"+
            "<if test = 'cat != null'>" +
            "and template.cat = #{cat} " +
            "</if>"+
            " </where>" +
            "<if test = 'sortField != null and sortMode != null' >" +
            "order by ${sortField} ${sortMode}" +
            "</if>" +
            "limit #{page},#{size} " +
            " </script>")
    List<PotManageTmpInfo> selectTemplateInfosByMultConditions(@Param("id")Integer id, @Param("state")Integer state,
                                                               @Param("cat")String cat, @Param("sortField")String sortField,
                                                               @Param("sortMode")String sortMode, @Param("page")Integer page, @Param("size")Integer size);

    @Select(
            "<script>" +
                    "SELECT " +
                    "count(1)" +
                    "from rawdatas.crawlconfig template " +
                    "<where >" +
                    "<if test = 'id != null'>" +
                    "and template.id = #{id} " +
                    "</if>" +
                    "<if test = 'state != null'>" +
                    "and template.state = #{state} " +
                    "</if>" +
                    "<if test = 'cat != null'>" +
                    "and template.cat = #{cat} " +
                    "</if>" +
                    " </where>" +
                    " </script>")
    Integer selectTotalCountByMultConditions(@Param("id") Integer id, @Param("state") Integer state,
                                             @Param("cat") String cat);

    @Select("select count(1) from rawdatas.configeditlog where cid = #{id} and attinfo='xiuding:修改'")
    Integer selectModifyTimesCountById(Integer id);

    @Select("select count(1) from rawdatas.sourcebeizhu where cid = #{id}")
    Integer selectCollectStrategyChangeTimesById(Integer id);



    }
