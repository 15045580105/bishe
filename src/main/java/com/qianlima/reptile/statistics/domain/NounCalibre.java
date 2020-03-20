package com.qianlima.reptile.statistics.domain;

import com.qianlima.reptile.statistics.utils.UUIDUtil;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: NounCalibre

 * @date 2020/3/20 10:09 上午
 */
@Document(collection = "NounCalibre")
@Data
public class NounCalibre {

    @Id
    private String id = UUIDUtil.primaryKey();
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private String operateTime;
    /**
     * 内容
     */
    private String content;
}
