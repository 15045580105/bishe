package com.yongxv.bishe.zhanshi.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 19:23 2020-05-11
 * @Modified By :
 */

import lombok.Data;

/**
 * @author gyx
 * @date 2020-05-11 19:23
 */
@Data
public class UserDto {

    private int id;

    private String userName;

    private String password;

    private String account;

    private String introduction;

    private String type;

    private String storeType;

    private String area;

    private String createTime;

    private String updateTime;

    private String picture;

    private String focusCount;

}
