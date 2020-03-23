package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: NounCalibreService
 * @date 2020/3/20 11:06 上午
 */
public interface NounCalibreService {
    /**
     * 名词口径增加接口
     * @param operator
     * @param content
     * @return
     */
    Response addNounCalibre(String operator,String content);

    /**
     * 名词口径更新接口
     * @param id
     * @param operator
     * @param content
     * @return
     */
    Response updateNounCalibre(String id,String operator,String content);

    /**
     * 名词口径删除接口
     * @param id
     * @return
     */
    Response deleteNounCalibre(String id);

    /**
     * 名词口径查询接口
     * @return
     */
    Response queryNounCalibre();
}
