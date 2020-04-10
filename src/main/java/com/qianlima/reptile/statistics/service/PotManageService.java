package com.qianlima.reptile.statistics.service;

import com.qianlima.reptile.statistics.entity.PotManageTmplReq;
import com.qianlima.reptile.statistics.entity.Response;

public interface PotManageService {
    Response getPotMangeTmplInfos(PotManageTmplReq potManageTmplReq);
}
