package com.yongxv.bishe.zhanshi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yongxv.bishe.zhanshi.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:46 2020-05-02
 * @Modified By :
 */

@Component
@DS("bishe")
public interface StoreMapper {

    @Select("select * from user where type = 2 and storeType = #{storeType} limit #{page},#{size}")
    List<User> selectStore(@Param("storeType") String storeType,@Param("page")int page,@Param("size")int size);


    @Select("select count(1) from user where type = 2 and storeType = #{storeType}")
    Integer selectStoreCount(@Param("storeType") String storeType);
}
