package com.qianlima.reptile.statistics.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
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
@DS("rawdata")
public interface RawdataMapper {

    @Select("select domain from rawdatas.ae_pot limit #{page},#{count}")
    List<String> selectPotByPage(@Param("page") String page, @Param("count") String count);

    @Select("select count(domain) from rawdatas.ae_pot")
    List<String> selectPotByTotalCount();

    @Select("select domain from rawdatas.ae_pot limit #{page},#{count}")
    List<String> selectTempltByPage(@Param("page") String page, @Param("count") String count);

}
