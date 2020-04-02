package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianlima.reptile.statistics.entity.PotDetail;
import com.qianlima.reptile.statistics.entity.PotNote;
import com.qianlima.reptile.statistics.entity.TemplateInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-11 10:18
 **/
@Component
@DS("qianlima")
public interface RawdatasMapper extends BaseMapper<Map> {

    @Select("select count(1) from rawdatas.`bidding_raw` where  intime between #{startUpdateTime} and #{endUpdateTime}")
    Integer select(@Param("startUpdateTime") int startUpdateTime, @Param("endUpdateTime") int endUpdateTime);

    /**
     * 查询采集量单日模版id
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select ae_template from rawdatas.bidding_raw where intime between #{startTime} and #{endTime}")
    List<String> selectTemplateIdInTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    @Select("SELECT ap.id,ap.name as name,ap.domain,pa.name as area, ap.creater ,ap.intime from rawdatas.ae_pot ap LEFT JOIN qianlima.phpcms_area pa on ap.area = pa.areaid where ap.domain = #{domain}")
    PotDetail selectPotDetailByPotName(String domain);

    @Select("select * from rawdatas.ccbeizhu where cid = #{id}")
    List<PotNote> selectPotNoteByPotId(Integer id);

    @Select("SELECT id, potName, isxm, cat, state , createtime, updateTime, collect_strategy FROM rawdatas.crawlconfig WHERE potName = #{potName}")
    List<TemplateInfo> selectTemplateInfosByName(String potName);

    @Select("SELECT id FROM rawdatas.crawlconfig WHERE potName = #{potName}")
    List<String> selectTemplateIdByName(String potName);
}