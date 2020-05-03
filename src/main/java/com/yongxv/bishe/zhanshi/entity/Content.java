package com.yongxv.bishe.zhanshi.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 18:56 2020-05-01
 * @Modified By :
 */

import lombok.Data;

/**
 * @author gyx
 * @date 2020-05-01 18:56
 */
@Data
public class Content {

    private int id;

    private int uid;

    private int toUser;

    private String content;

    private String createTime;

    private String shopState;

    private String customer;

}
