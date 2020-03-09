package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.KeyWordTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 关键词统计
 * @author: sx
 * @create: 2020-03-09 14:36:32
 **/
@Component
@DS("qianlima")
public interface FirstKeyWordMapper {

    @Select("select id from spiderplatform.website_account_keywords where websiteAccountId > 1100")
    List<Integer> selectId();

    @Select("select keywords,taskListUrls from spiderplatform.website_account_keywords where id = #{id}")
    KeyWordTask selectKeyWordsTaskById(@Param("id") Integer id);

    @Select("select COUNT(DISTINCT url) from spiderplatform.keyword_data_comparsion where web = #{web} and first_level_keyword = #{classify}  and publishTime >= #{startTime} and publishTime >=#{endTime}")
    Integer countUrl(@Param("web") String web, @Param("classify") String classify, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
