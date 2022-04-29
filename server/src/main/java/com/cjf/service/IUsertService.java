package com.cjf.service;

import com.cjf.model.UserT;

import java.util.List;
import java.util.Map;

// 用户服务接口

public interface IUsertService {
    public UserT userLogin(UserT userT);
    public void userRegister(UserT userT) throws Exception;
    public void userChangePass(UserT userT) throws Exception;
    public Map<String,Object> getUserList(int page,int pageSize,String searchValue);
    public boolean deleteUserById(int id);
    public  boolean updateUser(UserT userT);
    public  UserT getUserById(int id);
    public Map<String,Object> getArticleOverview(int page,int pageSize);
    public  void addArticle(int authorId);
    public  void deleteArticle(int authorId);
}
