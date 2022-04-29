package com.cjf.dao;

import com.cjf.model.ArticleT;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleMapper {
    List<ArticleT> getArticleList(@Param("skip") int skip, @Param("limit") int limit, @Param("searchValue") String searchValue,@Param("authorId") int authorId);

    // 获取文章总数
    int getArticleCount( @Param("searchValue") String searchValue, @Param("authorId") int authorId);

    // 删除文章
    void deleteArticleById(@Param("articleId") int articleId);
    // 根据用户删除文章
    void deleteArticleByAuthor(@Param("authorId") int authorId);

    // 创建文章
    void addArticle(ArticleT articleT);

    // 更新文章
    void updateArticle(ArticleT articleT);

}
