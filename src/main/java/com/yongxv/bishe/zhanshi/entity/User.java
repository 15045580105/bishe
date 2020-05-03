package com.yongxv.bishe.zhanshi.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 15:04 2020-05-01
 * @Modified By :
 */

import lombok.Data;

/**
 * @author gyx
 * @date 2020-05-01 15:04
 */
@Data
public class User {

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


}
