package com.cjf.model;


import lombok.Data;
import org.springframework.stereotype.Component;

@Component("ArticleT")
@Data
public class ArticleT {
    private int articleId;
    private int authorId;
    private String title;
    private String content;
}
