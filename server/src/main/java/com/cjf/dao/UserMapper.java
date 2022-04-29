package com.cjf.dao;

import com.cjf.model.UserT;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository //类识别为Bean，同时它还能将所标注的类中抛出的数据访问异常封装为 Spring 的数据访问异常类型
public interface UserMapper {
    List<UserT> getUserList(@Param("skip") int skip, @Param("limit") int limit, @Param("searchValue") String searchValue);

    // 获取符合搜索条件的用户条数
    int getUserCount(@Param("searchValue") String searchValue);

    // 增加
    void addUser(UserT u);

    // 查
    UserT getUserById(int id);

    // 改
    void updateUser(UserT u);

    // 删
    void deleteUser(int id);

    // 通过账号密码获取信息
    UserT login(UserT userT);

    UserT getUserByName(UserT userT);

    UserT getUserByEmail(UserT userT);

    // 获取每个用户文章数量
    List<Map<String,Object>> getArticleOverview(@Param("skip") int skip, @Param("limit") int limit);

    // 增加一个文章
    void addArticle(@Param("authorId") int authorId);

    //删除一篇文章
    void deleteArticle(@Param("authorId") int authorId);


}
