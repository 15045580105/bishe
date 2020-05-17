package com.yongxv.bishe.zhanshi.service;

import com.yongxv.bishe.zhanshi.entity.Response;

/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 09:41 2020-03-20
 * @Modified By :
 */
public interface UserService {

    Response login(String account, String passWord,String role);

    Response register(String account,String passWord,String type,String storeType,String userName,String introduction,String area);

    Response addAdmin(String account,String passWord,String userName,String introduction,String area);

    Response updateInformation(int id,String userName,String introduction,String area);

    Response updatePassWord(int id,String account,String password,String passwordAgain);

    Response selectUser(int id);

    Response focus(int uid,int uuid);

    Response deleteFocus(int uid,int uuid);

    Response selectFocusStore(int uid,int page,int size);

    Response selectFocusUser(int uuid,int page,int size);

    Response selectNewUpdate(int page,int size);

    Response selectAcess(int uid,int page,int size);

    Response addWarning(int uid,int toUser);

    Response deleteUser(int id);

    Response selectStore(int page,int size);

    Response selectConsumers(int page,int size);

    Response content(int uid,int toUser,String content);

    Response contentStore(int uid,String content);

    Response pushAndMessage(int id,int page,int size);

    Response receivedPushOrMessage(int id,int page,int size);

    Response deletePushAndMessage(int id,int uid);

    Response selectUserBypage(int id,int page,int size);

    Response deleteAccess(int id);

}
