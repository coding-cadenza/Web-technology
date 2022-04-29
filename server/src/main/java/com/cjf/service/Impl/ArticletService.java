package com.cjf.service.Impl;


import com.cjf.dao.ArticleMapper;
import com.cjf.model.ArticleT;
import com.cjf.model.UserT;
import com.cjf.service.IArticletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("articletService")
public class ArticletService implements IArticletService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UsertService usertService;

    @Override
    public Map<String,Object> getArtilceDetail(int page, int pageSize, String searchValue,int authorId){
        Map<String,Object> res = new HashMap<String,Object>();  // 返回的结果
        int count = articleMapper.getArticleCount(searchValue,authorId); // 获取总数
        res.put("total",count);
        System.out.println("获取文章数量成功"+count);
        List<ArticleT> articleTList = articleMapper.getArticleList((page-1)*pageSize,pageSize,searchValue,authorId);
        System.out.println("获取文章列表成功"+articleTList);
        res.put("articleDetail",articleTList);
        // 获取用户的信息
        UserT u = usertService.getUserById(authorId);
        System.out.println("获取用户信息成功"+u);
        res.put("userInfo",u);
        return  res;
    }

    /**
     * 删除一篇文章
     * @param articleId
     * @return
     */
    @Override
    public boolean deleteArticleById(int articleId,int authorId){
        try{
            articleMapper.deleteArticleById(articleId);
            usertService.deleteArticle(authorId);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public boolean addArticle(ArticleT articleT){
        try{
            // 增加一个文章
            articleMapper.addArticle(articleT);
            // 修改成功后根据Id增加一个文章
             usertService.addArticle(articleT.getAuthorId());
             return true;
        }catch (Exception e){
            return  false;
        }
    }

    @Override
    public boolean updateArticle(ArticleT articleT){
        // 更新文章
        try{
            System.out.println("开始更新文章信息");
            System.out.println(articleT.toString());

            articleMapper.updateArticle(articleT);
            System.out.println("文章信息更新完成");
            return true;
        }catch (Exception e){
            return  false;
        }
    }
    @Override
    public boolean deleteArticleByAuthor(int authorId){
        articleMapper.deleteArticleByAuthor(authorId);
        return true;
    }

}
