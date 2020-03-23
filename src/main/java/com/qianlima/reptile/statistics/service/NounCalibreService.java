package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.Response;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: NounCalibreService
 * @date 2020/3/20 11:06 上午
 */
public interface NounCalibreService {
    Response addNounCalibre(String operator,String content);

    Response updateNounCalibre(String id,String operator,String content);

    Response deleteNounCalibre(String id);

    Response queryNounCalibre();
}
