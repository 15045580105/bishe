package com.yongxv.bishe.zhanshi.controller;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 15:03 2020-05-02
 * @Modified By :
 */

import com.yongxv.bishe.zhanshi.entity.Response;
import com.yongxv.bishe.zhanshi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author gyx
 * @date 2020-05-02 15:03
 */
@RestController
@RequestMapping("/bishe")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/store/show")
    public Response storeShow(String storeType,Integer page,Integer size) {
        return storeService.showStoreService(storeType,page,size);
    }


    @PostMapping("/goods/show")
    public Response goodsShow(int uid,int uuid,Integer page,Integer size) {
        return storeService.showGoods(uid,uuid,page,size);
    }

    @PostMapping("/goods/delete")
    public Response goodsDelete(String id) {
        return storeService.delete(id);
    }

    @PostMapping("/goods/update")
    public Response goodsUpdate(String id,String name,String introduction,long price) {
        return storeService.updateGoods(id,name,introduction,price);
    }


    @PostMapping("/goods/add")
    public Response goodsAdd(String uid, String name, String introduction, String type,String price, MultipartFile file) {
        return storeService.addGoods(uid,name,introduction,type,price,file);
    }


    @PostMapping("/goods/select")
    public Response goodsSelect(String id) {
        return storeService.selectGoodsById(id);
    }

    @PostMapping("/goods/select/store")
    public Response goodsSelectStore(int uid,Integer page,Integer size) {
        return storeService.showGoodsStore(uid,page,size);
    }


}
