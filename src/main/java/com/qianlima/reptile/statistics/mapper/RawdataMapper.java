package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.qianlima.reptile.statistics.entity.TempltDo;
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
     *查询id ，state，和potname
     * @return
     */
    @Select("select id,state,potName from rawdatas.crawlconfig")
    List<TempltDo> selectTemplt();
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

}
