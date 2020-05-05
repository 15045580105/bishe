package com.yongxv.bishe.zhanshi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yongxv.bishe.zhanshi.entity.Access;
import com.yongxv.bishe.zhanshi.entity.Associated;
import com.yongxv.bishe.zhanshi.entity.Content;
import com.yongxv.bishe.zhanshi.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 16:10 2020-03-08
 * @Modified By :
 */
@Component
@DS("bishe")
public interface BisheMapper {
    /**
     *
     * @param account
     * @return
     */
    @Select("select * from user where account = #{account}")
    User selectUser(@Param("account") String account);

    @Select("select * from user where id = #{id}")
    User selectUserByid(@Param("id") int id);

    @Select("select * from user where type = 2 and updateTime >= #{startTime}")
    List<User> selectStoreByUpdatetime(@Param("startTime") String startTime);

    @Select("select * from user where type = 2 limit #{page},#{size}")
    List<User> selectStores(@Param("page")int page,@Param("size")int size);

    @Select("select * from user where type = 1 limit #{page},#{size}")
    List<User> selectConsumer(@Param("page")int page,@Param("size")int size);

    @Select("select count(1) from user where type = 1")
    Integer selectConsumers();

    @Select("select count(1) from user where type = 2")
    Integer selectStore();

    @Select("select count(1) from user where type = 2 and storeType = 0")
    long selectShoes();

    @Select("select count(1) from user where type = 2 and storeType = 1")
    long selectClothes();

    @Select("select count(1) from user where type = 2 and storeType = 2")
    long selectOthers();

    @Insert("insert into user(userName,password,account,introduction,type,storeType,area,createTime) " +
            "values(#{user.userName},#{user.password},#{user.account},#{user.introduction},#{user.type},#{user.storeType},#{user.area},#{user.createTime})")
    int addUser(@Param("user")User user);

    @Update("update user set  userName = #{user.userName},introduction = #{user.introduction},area = #{user.area} where id = ${user.id}")
    int updateInformation(@Param("user")User user);

    @Update("update user set   password = #{user.password} where id = #{user.id}")
    int updatePassword(@Param("user")User user);

    @Update("update user set  updateTime = #{user.updateTime} where id = #{user.id}")
    int updateUpdateTime(@Param("user")User user);

    @Insert("insert into associated(uid,uuid,createTime) " +
            "values(#{uid},#{uuid},#{createTime})")
    int addFocus(@Param("uid")int uid,@Param("uuid")int uuid,@Param("createTime")String createTime);

    @Delete("delete from associated where uid = #{uid} and uuid = #{uuid}")
    int deleteFocus(@Param("uid")int uid,@Param("uuid")int uuid);

    @Select("select * from associated where uid = #{uid} limit #{page},#{size}")
    List<Associated> selectFocus(@Param("uid")int uid,@Param("page")int page,@Param("size")int size);

    @Select("select count(1) from associated where uid = #{uid}")
    Integer selectFocusCount(@Param("uid")int uid);

    @Select("select * from associated where uid = #{uid} and uuid = #{uuid} ")
    List<Associated> selectGuaZhu(@Param("uid")int uid,@Param("uuid")int uuid);

    @Select("select * from associated where uuid = #{uuid} limit #{page},#{size}")
    List<Associated> selectFocusUser(@Param("uuid")int uuid,@Param("page")int page,@Param("size")int size);

    @Select("select count(1) from associated where uuid = #{uuid}")
    Integer selectFocusUserCount(@Param("uuid")int uuid);

    @Select("select * from associated where uuid = #{uuid}")
    List<Associated> selectFocusUserAll(@Param("uuid")int uuid);

    @Select("select * from access where uid = #{uid} limit #{page},#{size}")
    List<Access> selectAccess(@Param("uid")int uid,@Param("page")int page,@Param("size")int size);

    @Select("select count(1) from access where uid = #{uid}")
    Integer selectAccessCount(@Param("uid")int uid);


    @Insert("insert into access(uid,uuid,name,introduction,area,createTime) " +
            "values(#{uid},#{uuid},#{name},#{introduction},#{area},#{createTime})")
    int addAccess(@Param("uid")int uid,@Param("uuid")int uuid,@Param("name")String name,@Param("introduction")String introduction,@Param("area")String area,@Param("createTime")String createTime);


    @Insert("insert into content(uid,toUser,content,createTime,shopState,customer) " +
            "values(#{uid},#{toUser},#{content},#{createTime},0,0)")
    int addWarning(@Param("uid")int uid,@Param("toUser")int toUser,@Param("content")String content,@Param("createTime")String createTime);


    @Delete("delete from user where id = #{id}")
    int deleteUser(@Param("id")int id);

    @Insert("insert into content(uid,toUser,content,createTime,shopState,customer) " +
            "values(#{uid},#{toUser},#{content},#{createTime},0,0)")
    int insertContent(@Param("uid")int uid,@Param("toUser")int toUser,@Param("content")String content,@Param("createTime")String createTime);

    @Select("select * from content where uid = #{uid} and shopState = 0 limit #{page},#{size}")
    List<Content> push(@Param("uid")int uid, @Param("page")int page, @Param("size")int size);

    @Select("select count(1) from content where uid = #{uid} and shopState = 0")
    Integer pushCount(@Param("uid")int uid);

    @Select("select * from content where uid = #{uid} and customer = 0 limit #{page},#{size}")
    List<Content> message(@Param("uid")int uid, @Param("page")int page, @Param("size")int size);

    @Select("select count(1) from content where uid = #{uid} and customer = 0")
    Integer messageCount(@Param("uid")int uid);

    @Select("select * from content where toUser = #{toUser} and shopState = 0 limit #{page},#{size}")
    List<Content> receivedMessage(@Param("toUser")int toUser, @Param("page")int page, @Param("size")int size);

    @Select("select count(1) from content where toUser = #{toUser} and shopState = 0")
    Integer receivedMessageCount(@Param("toUser")int toUser);

    @Select("select * from content where uuid = #{uuid} and customer = 0 limit #{page},#{size}")
    List<Content> receivedPush(@Param("uuid")int uuid, @Param("page")int page, @Param("size")int size);

    @Select("select count(1) from content where uuid = #{uuid} and customer = 0")
    Integer receivedPushCount(@Param("uuid")int uuid);

    @Update("update content set shopState = 1 where id = #{id}")
    int updateShopState(@Param("id")int id);

    @Update("update content set customer = 1 where id = #{id}")
    int updateCustomer(@Param("id")int id);


}
