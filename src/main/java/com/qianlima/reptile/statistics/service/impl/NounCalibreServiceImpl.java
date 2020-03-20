package com.qianlima.reptile.statistics.service.impl;

import com.qianlima.reptile.statistics.domain.NounCalibre;
import com.qianlima.reptile.statistics.entity.Response;
import com.qianlima.reptile.statistics.repository.NounCalibreRepostory;
import com.qianlima.reptile.statistics.service.NounCalibreService;
import com.qianlima.reptile.statistics.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: NounCalibreServiceImp
 * @date 2020/3/20 11:08 上午
 */
@Service
public class NounCalibreServiceImpl implements NounCalibreService {
    @Autowired
    private NounCalibreRepostory nounCalibreRepostory;

    @Override
    public Response addNounCalibre(String operator,String content) {
        NounCalibre nounCalibre = new NounCalibre();
        nounCalibre.setOperateTime(DateUtils.getFormatDateStr(System.currentTimeMillis(), DateUtils.EXACTSDF));
        nounCalibre.setContent(content);
        nounCalibre.setOperator(operator);
        return Response.success(nounCalibreRepostory.save(nounCalibre));
    }

    @Override
    public Response updateNounCalibre(String id,String operator,String content) {
        NounCalibre nounCalibre = new NounCalibre();
        nounCalibre.setOperateTime(DateUtils.getFormatDateStr(System.currentTimeMillis(), DateUtils.EXACTSDF));
        nounCalibre.setOperator(operator);
        nounCalibre.setContent(operator);
        nounCalibre.setId(id);

        return Response.success(nounCalibreRepostory.update(nounCalibre).getModifiedCount());
    }

    @Override
    public Response deleteNounCalibre(String id) {
        return Response.success(nounCalibreRepostory.delete(id).getDeletedCount());
    }
}
