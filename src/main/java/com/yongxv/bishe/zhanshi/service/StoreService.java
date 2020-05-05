package com.yongxv.bishe.zhanshi.service;

import com.yongxv.bishe.zhanshi.entity.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:45 2020-05-02
 * @Modified By :
 */
public interface StoreService {

    Response showStoreService(String storeType,int page,int size);

    Response showGoods(int uid,int uuid,Integer page,Integer size);

    Response showGoodsStore(int uid,Integer page,Integer size);

    Response delete(String id);

    Response updateGoods(String id,String name,String introduction,long price);

    Response addGoods(String uid, String name, String introduction, String type,String price, MultipartFile file);

    Response selectGoodsById(String id);

}
