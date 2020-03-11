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
@DS("spiderplatform")
public interface SpiderPlatformMapper1 {

    @Select("select id from website_account_keywords where websiteAccountId > 1100")
    List<Integer> selectId();

    @Select("select keywords,taskListUrls from website_account_keywords where id = #{id}")
    KeyWordTask selectKeyWordsTaskById(@Param("id") Integer id);

    @Select("select keywords from website_account_keywords where id = #{keywordId}")
    String selectKeywordsById(Integer keywordId);



}
