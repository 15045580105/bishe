package com.qianlima.reptile.statistics.domain;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 15:11 2020-03-18
 * @Modified By :
 */

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gyx
 * @date 2020-03-18 15:11
 */
@Document(collection = "tmpltAndPotStatistics")
@Data
public class TmpltAndPotStatistics {
    @Id
    private ObjectId id;

    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;
    /**
     * pot总量
     */
    private long potTotal;
    /**
     * pot启用量
     */
    private long potUsing;
    /**
     * pot废弃量
     */
    private long potAbandoned;
    /**
     * pot异常量
     */
    private long potAbnormal;
    /**
     * pot新建
     */
    private long potNew;
    /**
     * 模版总量
     */
    private long templateTotal;
    /**
     * 模版启用量
     */
    private long templateUsing;
    /**
     * 模版待启用量
     */
    private long templatToEnable;
    /**
     * 模版删除总量
     */
    private long templateDelete;
    /**
     * 模版未分类总量
     */
    private long templateunClassified;
    /**
     * 模版异常总量
     */
    private long templateAbnormal;
    /**
     * 日期标示，0非月末，1月末
     */
    private long endOfTheMonth;

    public TmpltAndPotStatistics() {
    }

    public TmpltAndPotStatistics(String queryDate) {
        this.queryDate = queryDate;
    }
}
