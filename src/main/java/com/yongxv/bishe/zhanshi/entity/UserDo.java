package com.yongxv.bishe.zhanshi.entity;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 20:16 2020-05-01
 * @Modified By :
 */

import lombok.Data;

/**
 * @author gyx
 * @date 2020-05-01 20:16
 */
@Data
public class UserDo {

    private int id;

    private String userName;

    private String type;

    private long storeNumber;

    private long userNumber;

    private long consumers;

    private long goods;

    private String shoes;

    private String clothes;

    private String others;

    private String shoesGoods;

    private String clothesGoods;

    private String othersGoods;
}
