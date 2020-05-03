package com.yongxv.bishe.zhanshi.service.impl;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:46 2020-03-20
 * @Modified By :
 */

import com.yongxv.bishe.zhanshi.entity.*;
import com.yongxv.bishe.zhanshi.mapper.BisheMapper;
import com.yongxv.bishe.zhanshi.repository.GoodsRepository;
import com.yongxv.bishe.zhanshi.service.UserService;
import com.yongxv.bishe.zhanshi.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gyx
 * @date 2020-03-20 09:46
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BisheMapper bisheMapper;
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public Response login(String account, String passWord){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(passWord)){
            return Response.error(1,"请输入账号或者密码");
        }else{
            User user = bisheMapper.selectUser(account);
            if(user == null || "".equals(user)){
                return Response.error(1,"用户不存在");
            }else{
                if(user.getPassword().equals(passWord)){
                    UserDo userDo = new UserDo();
                    long consumers = bisheMapper.selectConsumers();
                    long store = bisheMapper.selectStore();
                    long shoes = bisheMapper.selectShoes();
                    long clothes = bisheMapper.selectClothes();
                    long others = bisheMapper.selectOthers();
                    long shoesGoods = goodsRepository.query("0");
                    long clothesGoods = goodsRepository.query("1");
                    long othersGoods = goodsRepository.query("2");
                    userDo.setId(user.getId());
                    userDo.setUserName(user.getUserName());
                    userDo.setType(user.getType());
                    userDo.setStoreNumber(store);
                    userDo.setUserNumber(store+consumers);
                    userDo.setConsumers(consumers);
                    long goodsTotal = shoesGoods+clothesGoods+othersGoods;
                    userDo.setGoods(goodsTotal);
                    userDo.setShoes(efficient(store,shoes));
                    userDo.setClothes(efficient(store,clothes));
                    userDo.setOthers(efficient(store,others));
                    userDo.setShoesGoods(efficient(goodsTotal,shoesGoods));
                    userDo.setClothesGoods(efficient(goodsTotal,clothesGoods));
                    userDo.setOthersGoods(efficient(goodsTotal,othersGoods));
                    return Response.success(userDo,"登陆成功");
                }else {
                    return Response.error(1,"账号或密码错误");
                }
            }
        }
    }


    @Override
    public Response register(String account,String passWord,String type,String storeType,String userName,String introduction,String area){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(passWord)){
            return Response.error(1,"账号或密码不能为空");
        }
        if(account.length() < 6 || passWord.length() < 6){
            return Response.error(1,"账号或和密码长度必需大于6位");
        }
        if(account.length() >15 || passWord.length() > 15){
            return Response.error(1,"账号或和密码长度必需小于15位");
        }
        if(!isNumeric(account)){
            return Response.error(1,"账号只能由纯数字组成");
        }
        User user = bisheMapper.selectUser(account);
        if(user == null || "".equals(user)){
            User user1 = new User();
            String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
            user1.setAccount(account);
            user1.setPassword(passWord);
            user1.setType(type);
            if(type.equals("2")){
                user1.setStoreType(storeType);
            }else{
                user1.setStoreType("");
            }
            user1.setUserName(userName);
            user1.setIntroduction(introduction);
            user1.setArea(area);
            user1.setCreateTime(today);
            bisheMapper.addUser(user1);
            return Response.success(0,"注册成功");
        }else{
            return Response.error(1,"此账号已存在");
        }

    }


    @Override
    public Response updateInformation(int id,String userName,String introduction,String area){
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setIntroduction(introduction);
        user.setArea(area);
        bisheMapper.updateInformation(user);
        User user1 = bisheMapper.selectUserByid(id);
        return Response.success(user1);
    }


    @Override
    public Response updatePassWord(int id,String account,String password,String passwordAgain){
        User user = bisheMapper.selectUserByid(id);
        if(!user.getAccount().equals(account)){
            return Response.error(1,"账号输入不正确");
        }else if(!password.equals(passwordAgain)){
            return Response.error(1,"两次密码输入不相同");
        }else if(password.length() < 6){
            return Response.error(1,"密码必须大于6位");
        }else if(password.length() > 15){
            return Response.error(1,"密码必须小于15位");
        }else {
            User user1 = new User();
            user1.setId(id);
            user1.setPassword(password);
            bisheMapper.updatePassword(user1);
            return Response.success("","修改成功请重新登陆");
        }

    }

    @Override
    public Response selectUser(int id){
        User user  = bisheMapper.selectUserByid(id);
        return Response.success(user);
    }

    @Override
    public Response focus(int uid,int uuid){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        bisheMapper.addFocus(uid,uuid,today);
        return Response.success(0,"关注成功");
    }

    @Override
    public Response deleteFocus(int uid,int uuid){
        bisheMapper.deleteFocus(uid,uuid);
        return Response.success(0,"取消关注成功");
    }

    @Override
    public Response selectFocusStore(int uid,int page,int size){
        List<Associated> associatedList = bisheMapper.selectFocus(uid,page*size,size);
        List<User> users = new ArrayList<>();
        for (int i = 0; i <associatedList.size() ; i++) {
            User user  = bisheMapper.selectUserByid(associatedList.get(i).getUuid());
            users.add(user);
        }
        return Response.success(users);
    }

    @Override
    public Response selectFocusUser(int uuid,int page,int size){
        List<Associated> associatedList = bisheMapper.selectFocusUser(uuid,page*size,size);
        List<User> users = new ArrayList<>();
        for (int i = 0; i <associatedList.size() ; i++) {
            User user  = bisheMapper.selectUserByid(associatedList.get(i).getUid());
            users.add(user);
        }
        return Response.success(users);
    }

    @Override
    public Response selectNewUpdate(){
        String today = DateUtil.getDate(new Date());
        String startTime = today+" 00:00:00";
        List<User> associatedList = bisheMapper.selectStoreByUpdatetime(startTime);
        return Response.success(associatedList);
    }
    @Override
    public Response selectAcess(int uid,int page,int size){
        List<Access> accessList = bisheMapper.selectAccess(uid,page*size,size);
        return Response.success(accessList);
    }

    @Override
    public Response addWarning(int uid,int toUser){
        String content = "您有数据不符合标准请尽快修改，以免封号";
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        bisheMapper.addWarning(uid,toUser,content,today);
        return Response.success(0);
    }
    @Override
    public Response deleteUser(int id){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("2")){
            goodsRepository.delete(id+"");
        }
        bisheMapper.deleteUser(id);
        return Response.success(0);
    }

    @Override
    public Response selectStore(int page,int size){
        List<User> userList = bisheMapper.selectStores(page*size,size);
        return Response.success(userList);
    }
    @Override
    public Response selectConsumers(int page,int size){
        List<User> userList = bisheMapper.selectConsumer(page*size,size);
        return Response.success(userList);
    }



    @Override
    public Response content(int uid,int toUser,String content){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        bisheMapper.insertContent(uid,toUser,content,today);
        return Response.success(0);
    }
    @Override
    public Response contentStore(int uid,String content){
        String today = DateUtil.getDateTime(DateUtil.getTimePattern(), new Date());
        List<Associated> associatedList = bisheMapper.selectFocusUserAll(uid);
        for (int i = 0; i <associatedList.size() ; i++) {
            bisheMapper.insertContent(uid,associatedList.get(i).getUid(),content,today);
        }
        return Response.success(0);
    }


    @Override
    public Response pushAndMessage(int id,int page,int size){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("1")){
            List<Content> contentList =  bisheMapper.message(id,page*size,size);
            return Response.success(contentList);
        }else{
            List<Content> contentList =  bisheMapper.push(id,page*size,size);
            return Response.success(contentList);
        }

    }
    @Override
    public Response receivedPushOrMessage(int id,int page,int size){
        User user = bisheMapper.selectUserByid(id);
        if(user.getType().equals("1")){
            List<Content> contentList =  bisheMapper.receivedPush(id,page*size,size);
            return Response.success(contentList);
        }else{
            List<Content> contentList =  bisheMapper.receivedMessage(id,page*size,size);
            return Response.success(contentList);
        }
    }



    public Response deletePushAndMessage(int id,int uid){
        User user = bisheMapper.selectUserByid(uid);
        if(user.getType().equals("1")){
            bisheMapper.updateCustomer(id);
        }else {
            bisheMapper.updateShopState(id);
        }
        return Response.success(0);
    }


    /**
     * @return a
     * @description 计算比例
     * @author gyx
     * @date 2020-03-23 18:5
     * @parameter * @param null
     * @since
     */
    private String efficient(long total, long count) {
        if(total==0||count==0){
            return "0";
        }
        double s = total;
        double c = count;
        double f = c / s;
        return String.format("%.4f", f);
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
