package com.yongxv.bishe.zhanshi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yongxv.bishe.zhanshi.entity.Associated;
import com.yongxv.bishe.zhanshi.entity.AssociatedDto;
import com.yongxv.bishe.zhanshi.entity.User;
import com.yongxv.bishe.zhanshi.entity.UserDto;
import org.apache.ibatis.annotations.Delete;
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
    List<UserDto> selectStore(@Param("storeType") String storeType, @Param("page")int page, @Param("size")int size);

    @Select("select * from user where type = 2 order by createTime desc limit #{page},#{size} ")
    List<UserDto> selectNewStore(@Param("page")int page, @Param("size")int size);

    @Select("select * from user where type = 2 order by focusCount desc limit #{page},#{size}")
    List<UserDto> selectHot(@Param("page")int page, @Param("size")int size);

    @Select("select count(1) from user where type = 2 and storeType = #{storeType}")
    Integer selectStoreCount(@Param("storeType") String storeType);

    @Delete("delete from access where uid = #{uid}")
    int deleteAccessRen(@Param("uid")int uid);

    @Delete("delete from access where uuid = #{uuid}")
    int deleteAccessDian(@Param("uuid")int uuid);

    @Delete("delete from associated where uid = #{uid}")
    int deleteAssociatedRen(@Param("uid")int uid);

    @Delete("delete from associated where uuid = #{uuid}")
    int deleteAssociatedDian(@Param("uuid")int uuid);

    @Delete("delete from content where uid = #{uid}")
    int deleteContentDian(@Param("uid")int uid);

    @Delete("delete from content where toUser = #{toUser}")
    int deleteContentRen(@Param("toUser")int toUser);

    @Select("select * from associated where uid = #{uid}")
    List<Associated> selectFocusAll(@Param("uid")int uid);
}
