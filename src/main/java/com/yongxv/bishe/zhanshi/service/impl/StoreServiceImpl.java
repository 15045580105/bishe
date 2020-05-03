package com.yongxv.bishe.zhanshi.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 14:45 2020-05-02
 * @Modified By :
 */

import com.yongxv.bishe.zhanshi.domain.Goods;
import com.yongxv.bishe.zhanshi.entity.Associated;
import com.yongxv.bishe.zhanshi.entity.Response;
import com.yongxv.bishe.zhanshi.entity.User;
import com.yongxv.bishe.zhanshi.mapper.BisheMapper;
import com.yongxv.bishe.zhanshi.mapper.StoreMapper;
import com.yongxv.bishe.zhanshi.repository.GoodsRepository;
import com.yongxv.bishe.zhanshi.service.StoreService;
import com.yongxv.bishe.zhanshi.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gyx
 * @date 2020-05-02 14:45
 */
@Service
public class StoreServiceImpl implements StoreService {
    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private BisheMapper bisheMapper;

    @Override
    public Response showStoreService(String storeType,int page,int size){
        List<User> users = storeMapper.selectStore(storeType,page*size,size);
        return Response.success(users);
    }

    @Override
    public Response showGoods(int uid,int uuid,Integer page,Integer size){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        User user = bisheMapper.selectUserByid(uuid);
        bisheMapper.addAccess(uid,uuid,user.getUserName(),user.getIntroduction(),user.getArea(),today);
        List<Goods> goodsList = goodsRepository.queryByUid(uuid+"",page,size);
        List<Associated> associatedList = bisheMapper.selectGuaZhu(uid,uuid);
        if(associatedList.size()==0){
            return Response.success(goodsList,"未关注");
        }else{
            return Response.success(goodsList,"已关注");
        }

    }

    @Override
    public Response delete(String id){
        goodsRepository.delete(id);
        return Response.success(0);
    }

    @Override
    public Response updateGoods(String id,String name,String introduction,long price){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        Goods goods = new Goods();
        goods.setId(id);
        goods.setName(name);
        goods.setIntroduction(introduction);
        goods.setUpdateTime(today);
        if(isNumeric(price+"")){
            if(price >= 0 && price<=20000){
                goods.setPrice(price);
            }else {
                return Response.error(1,"价格不可小于0或者大于20000");
            }
        }else{
            return Response.error(1,"价格只能是数字");
        }
        goodsRepository.update(goods);
        Goods goods1  = goodsRepository.queryById(id);
        User user = new User();
        user.setId(Integer.parseInt(goods1.getUid()));
        user.setUpdateTime(today);
        bisheMapper.updateUpdateTime(user);
        return Response.success(0);
    }

    @Override
    public Response addGoods(String uid, String name, String introduction, String type,String price, MultipartFile file){
        if (file == null || "".equals(file)) {
            return Response.error(1,"请插入图片");
        }
        User user = bisheMapper.selectUserByid(Integer.parseInt(uid));
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        Goods goods = new Goods();
        String url = getUrl(file);
        goods.setUid(uid);
        goods.setPicture(url);
        if(isNumeric(price)){
            if(Integer.parseInt(price) >= 0 && Integer.parseInt(price)<=20000){
                goods.setPrice(Integer.parseInt(price));
            }else {
                return Response.error(1,"价格不可小于0或者大于20000");
            }
        }else{
            return Response.error(1,"价格只能是数字");
        }
        goods.setIntroduction(introduction);
        goods.setType(type);
        goods.setName(name);
        goods.setStoreType(user.getStoreType());
        goods.setCreateTime(today);
        goods.setUpdateTime(today);
        goodsRepository.save(goods);
        user.setUpdateTime(today);
        bisheMapper.updateUpdateTime(user);
        return Response.success(0,"添加商品成功");
    }

    @Override
    public Response selectGoodsById(String id){
        Goods goods = goodsRepository.queryById(id);
        return Response.success(goods);
    }



    private String getUrl(MultipartFile file) {
        if (file.isEmpty()) {
            return "";
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File("/Users/gaomac/domesticOutfit/images" + "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
