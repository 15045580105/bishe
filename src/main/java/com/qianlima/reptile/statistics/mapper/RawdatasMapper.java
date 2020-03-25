package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * in查询bidding_raw表中对应id数据条数
     * @param ids
     * @param startTime
     * @param endTime
     * @return
     */
    @Select({
            "<script>" +
                    "select COUNT(1) from rawdatas.bidding_raw where ae_template in ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            " and intime > #{startTime} and intime &lt; #{endTime} "+
                    "</script>"
    })
    Integer selectBiddingCountsByIds(@Param("ids") List<String> ids, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    @Select("select ae_template from rawdatas.bidding_raw where intime between #{startTime} and #{endTime} limit #{limit} , 1000 ")
    List<String> selectTemplateIdInTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime,@Param("limit")Integer limit);
}