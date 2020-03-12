package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.KeyWordData;
import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @description: 二级关键词统计
 * @author: sx
 * @create: 2020-03-11 09:36:32
 **/
@Component
@DS("rawdatas")
public interface RawDatasMapper1 {

    @Select("select COUNT(DISTINCT url) from spiderplatform.keyword_data_comparsion where web = #{web} and first_level_keyword = #{classify}  and publishTime >= #{startTime} and publishTime <=#{endTime}")
    Integer countUrl(@Param("web") String web, @Param("classify") String classify, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select count(1) from spiderplatform.keyword_data_comparsion where web = #{web} and second_level_keyword = #{keyword} and publishTime >= #{startTime} and publishTime <= #{endTime}")
    Integer countSecondKeyWord(@Param("web") String web, @Param("keyword") String keyword, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select title,url,area,type,status from spiderplatform.keyword_data_comparsion where web = #{keyWordDataDetailReq.web} and second_level_keyword = #{keyWordDataDetailReq.keyword} and publishTime >= #{keyWordDataDetailReq.startTime} and publishTime <= #{keyWordDataDetailReq.endTime}")
    List<KeyWordData> dataDetail(@Param("keyWordDataDetailReq") KeyWordDataDetailReq keyWordDataDetailReq);

}
