package com.yongxv.bishe.zhanshi.domain;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:41 2020-05-01
 * @Modified By :
 */

import com.yongxv.bishe.zhanshi.utils.UUIDUtil;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gyx
 * @date 2020-05-01 14:41
 */
@Document(collection = "goods")
@Data
public class Goods {
    @Id
    private String id = UUIDUtil.primaryKey();

    /**
     * 店铺id
     */
    private String uid;

    /**
     * 图片
     */
    private String picture;

    /**
     * 价格
     */
    private long price;

    /**
     * 简介
     */
    private String introduction;

    /**
     *类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 店铺类型
     */
    private String storeType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

}
