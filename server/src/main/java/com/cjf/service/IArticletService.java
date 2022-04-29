package com.cjf.service;

import com.cjf.model.ArticleT;

import java.util.Map;

public interface IArticletService {
    public Map<String,Object> getArtilceDetail(int page,int pageSize,String searchValue,int authorId);
    public boolean deleteArticleById(int articleId,int authorId);
    public boolean addArticle(ArticleT articleT);
    public boolean deleteArticleByAuthor(int authorId);
    public boolean updateArticle(ArticleT articleT);
}
