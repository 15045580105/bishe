package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.KeyWordData;
import com.qianlima.reptile.statistics.entity.KeyWordDataDetailReq;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 关键词统计详情
 * @author: sx
 * @create: 2020-03-11 11:10:25
 **/
@Component
@DS("qianlima")
public interface KeyWordDataDetailMapper {
    @Select("select title,url,area,type,status from spiderplatform.keyword_data_comparsion where web = #{web} and second_level_keyword = #{keyword} and publishTime >= #{startTime} and publishTime <= #{endTime}")
    List<KeyWordData> dataDetail(KeyWordDataDetailReq keyWordDataDetailReq);

}
