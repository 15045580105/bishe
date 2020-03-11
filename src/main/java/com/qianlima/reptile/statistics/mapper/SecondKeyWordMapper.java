package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * @description: 二级关键词统计
 * @author: sx
 * @create: 2020-03-11 09:36:32
 **/
@Component
@DS("qianlima")
public interface SecondKeyWordMapper {

    @Select("select keywords from spiderplatform.website_account_keywords where id = #{keywordId}")
    String selectKeywordsById(Integer keywordId);

    @Select("select count(1) from spiderplatform.keyword_data_comparsion where web = #{web} and second_level_keyword = #{keyword} and publishTime >= #{startTime} and publishTime >= #{endTime}")
    Integer countSecondKeyWord(@Param("web") String web, @Param("keyword") String keyword, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
