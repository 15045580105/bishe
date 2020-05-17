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


    /**
     * @return a
     * @description 店铺展示
     * @author gyx
     * @date 2020-05-11 19:45
    * @parameter * @param null
     * @since
     */
    @PostMapping("/store/show")
    public Response storeShow(String storeType,Integer page,Integer size) {
        return storeService.showStoreService(storeType,page,size);
    }

    /**
     * @return a
     * @description 商品展示
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/show")
    public Response goodsShow(int uid,int uuid,Integer page,Integer size) {
        return storeService.showGoods(uid,uuid,page,size);
    }

    /**
     * @return a
     * @description 删除商品
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/delete")
    public Response goodsDelete(String id) {
        return storeService.delete(id);
    }

    /**
     * @return a
     * @description 修改商品信息
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/update")
    public Response goodsUpdate(String id,String name,String introduction,String price) {
        return storeService.updateGoods(id,name,introduction,price);
    }

    /**
     * @return a
     * @description 添加商品
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/add")
    public Response goodsAdd(String uid, String name, String introduction, String type,String price, MultipartFile file) {
        return storeService.addGoods(uid,name,introduction,type,price,file);
    }

    /**
     * @return a
     * @description  id查商品
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/select")
    public Response goodsSelect(String id) {
        return storeService.selectGoodsById(id);
    }

    /**
     * @return a
     * @description 查一个店铺的商品
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/goods/select/store")
    public Response goodsSelectStore(int uid,Integer page,Integer size) {
        return storeService.showGoodsStore(uid,page,size);
    }

    /**
     * @description 查询商品
     * @author gyx
     * @date 2020-05-11 20:31
     * @return
     * @parameter  * @param null
     * @since
     */
    @PostMapping("/goods/all")
    public Response goodsAll(Integer page,Integer size) {
        return storeService.selectGoods(page,size);
    }



    /**
     * @return a
     * @description 查询新注册的店
     * @author gyx
     * @date 2020-05-11 19:45
     * @parameter * @param null
     * @since
     */
    @PostMapping("/select/store/new")
    public Response SelectStoreNew(Integer page,Integer size) {
        return storeService.selectStoreNew(page,size);
    }


    /**
     * @description 最火的店
     * @author gyx
     * @date 2020-05-11 20:08
     * @return
     * @parameter  * @param null
     * @since
     */
    @PostMapping("/select/store/hot")
    public Response SelectStoreHot(Integer page,Integer size) {
        return storeService.selectStoreHot(page,size);
    }

}
