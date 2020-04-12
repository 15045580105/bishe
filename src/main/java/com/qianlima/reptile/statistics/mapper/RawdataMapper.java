package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:54 2020-03-18
 * @Modified By :
 */
@Component
@DS("modmonitor")
public interface RawdataMapper {
    /**
     * 查询全部pot
     * @return
     */
    @Select("select domain from rawdatas.ae_pot")
    List<String> selectAllPot();
    /**
     * 查询全部pot
     * @return
     */
    @Select("select domain as potName,intime as initTime,uptime as updateTime from rawdatas.ae_pot")
    List<PotDo> selectPot();


    /**
     *查询id ，state，和potname
     * @return
     */
    @Select("select id,state,potName from rawdatas.crawlconfig")
    List<TempltDo> selectTemplt();

    /**
     *查询id ，state，和potname
     * @return
     */
    @Select("select id as ids,state,potName from rawdatas.crawlconfig")
    List<TempltDo> selectTempt();
    /**
     *查询启用模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where state = 1")
    long selectEnableTemplt();
    /**
     *查询待启用模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where state = 0")
    long selectToEnableTemplt();
    /**
     *查询未分类模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where collect_strategy is null")
    long selectUnclassifiedTemplt();

    /**
     * @return a
     * @description 模版详情
     * @author gyx
     * @date 2020-04-02 21:0
     * @parameter * @param null
     * @since
     */
    @Select("select id,user,potName,isxm as isXm,state,cat,createtime as createTime,updateTime,collect_strategy as collectStrategy,configdatas as configData from rawdatas.crawlconfig where id =  ${id}")
    CrawlconfigDo selectCrawConfigByid(@Param("id") String id);

    /**
     * @return a
     * @description 状态变更查询
     * @author gyx
     * @date 2020-04-02 21:0
     * @parameter * @param null
     * @since
     */
    @Select("select intime as inTime,user,attinfo as attInfo from rawdatas.configeditlog where cid = ${id} order by id desc limit 10")
    List<ConfigedItLogDo> selectConfigeditLog(@Param("id") String id);

    /**
     * @return a
     * @description 查询备注
     * @author gyx
     * @date 2020-04-02 21:3
     * @parameter * @param null
     * @since
     */
    @Select("select * from rawdatas.ccbeizhu where cid = ${id} and type = 2 order by id desc limit 10")
    List<NoteDo> selectNote(@Param("id") String id);

    /**
     *查询启用模版数状态为
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where state = -2 and collect_strategy is not null and create_time  > #{startTime} and create_time <= #{endTime}")
    long selectEnableTempltByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     *查询启用模版id
     * @return
     */
    @Select("select id from rawdatas.crawlconfig where state = 1 and create_time  > #{startTime} and create_time <= #{endTime}")
    List<String> selectEnableTempltTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     *查询待启用模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where state = 0 and create_time  > #{startTime} and create_time <= #{endTime}")
    long selectToEnableTempltByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     *查询未分类模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where collect_strategy is null and state = -2 and create_time  > #{startTime} and create_time <= #{endTime}")
    long selectUnclassifiedTempltByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     *查询删除模版数
     * @return
     */
    @Select("select count(1) from rawdatas.crawlconfig where state = -1 and create_time  > #{startTime} and create_time <= #{endTime}")
    long selectDeleteTempltByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
