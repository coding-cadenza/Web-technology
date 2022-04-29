package com.cjf.service.Impl;

import com.cjf.dao.UserMapper;
import com.cjf.model.UserT;
import com.cjf.service.IUsertService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("usertService")
public class UsertService implements IUsertService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticletService articletService;

    @Override
    public UserT userLogin(UserT userT) {
        UserT loginUser = userMapper.login(userT);
        return loginUser;   // 直接返回，由controller判断
    }

    @Override
    public  void  userRegister(UserT userT) throws Exception{
        System.out.println(userT);
        if(userMapper.getUserByName(userT)!=null){
            throw  new Exception("用户名已被注册");
        }

        if(userMapper.getUserByEmail(userT)!=null){
            throw  new Exception("邮箱已经被注册");
        }
        // 添加用户
        System.out.println("开始添加用户");
        userMapper.addUser(userT);
    }
    @Override
    public void userChangePass(UserT userT) throws Exception{
        UserT u = userMapper.getUserByName(userT);

        if(u==null){
            throw new Exception("用户不存在");
        }
        // 重置密码
        u.setPassword(userT.getPassword());
        userMapper.updateUser(u);
    }

    @Override
    // 获取符合搜索条件的用户，返回总数以及当前页的用户信息
    public Map<String,Object> getUserList(int page, int pageSize, String searchValue){
        Map<String,Object> res = new HashMap<String,Object>();  // 返回的结果
        int count = userMapper.getUserCount(searchValue); // 获取总数
        res.put("total",count);
        List<UserT> uList = userMapper.getUserList((page-1)*pageSize,pageSize,searchValue);
        res.put("userData",uList);
        return  res;
    }

    @Override
    public boolean deleteUserById(int id){
        try{
            userMapper.deleteUser(id);
            articletService.deleteArticleByAuthor(id);
            return  true;
        }catch (Exception e){
            return false;
        }

    }
    @Override
    public  boolean updateUser(UserT userT){
        try{
            userMapper.updateUser(userT);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public  UserT getUserById(int id){
        return userMapper.getUserById(id);

    }

    @Override
    public Map<String,Object> getArticleOverview(int page,int pageSize){
        // 获取所有用户的信息，每条信息是其id,username,articleCount
         int total = userMapper.getUserCount(""); // 获取总数
         Map<String,Object> res = new HashMap<String,Object>();
         List<Map<String,Object>> resList = userMapper.getArticleOverview((page-1)*pageSize,pageSize);
         res.put("articleOverview",resList);
         res.put("total",total);
         return res;
    }

    @Override
    public  void addArticle(int authorId){
        userMapper.addArticle(authorId);
    }

    @Override
    public  void deleteArticle(int authorId){
        System.out.println("文章数量-1");
        userMapper.deleteArticle(authorId);
        System.out.println("文章数量-1成功");
    }
}

