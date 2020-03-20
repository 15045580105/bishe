package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.FaultTmpltDo;
import com.qianlima.reptile.statistics.entity.TempltDo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:54 2020-03-18
 * @Modified By :
 */
@Component
@DS("modmonitor")
public interface RawdataMapper {

    @Select("select domain from rawdatas.ae_pot limit #{page},#{count}")
    List<String> selectPotByPage(@Param("page") Integer page, @Param("count") Integer count);

    @Select("select count(domain) from rawdatas.ae_pot")
    long selectPotTotalCount();


    @Select("select id,state from rawdatas.crawlconfig")
    List<TempltDo> selectTemplt();

    @Select("select count(1) from rawdatas.crawlconfig")
    long selectTempltTotal();

    @Select("select count(1) from rawdatas.crawlconfig where state = 1")
    long selectEnableTemplt();

    @Select("select count(1) from rawdatas.crawlconfig where state = 0")
    long selectToEnableTemplt();

    @Select("select count(1) from rawdatas.crawlconfig where collect_strategy is null")
    long selectUnclassifiedTemplt();

    @Select("select id from rawdatas.crawlconfig where potName = #{name}")
    List<String> selectTmpltByPot(@Param("name") String name);

}
