package com.jluslda.edu.service;

import com.jluslda.edu.mapper.CategoryMapper;
import com.jluslda.edu.mapper.ContentMapper;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    CategoryMapper categoryMapper;
    ContentMapper contentMapper;
    List<String> categories;

    public ArticleService(CategoryMapper categoryMapper, ContentMapper contentMapper) {
        this.categoryMapper = categoryMapper;
        this.contentMapper = contentMapper;
        categories = categoryMapper.getCategory("article");
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addArticle(Content content) {
        content.setObject("article");
        contentMapper.addContent(content);
    }

    public Integer getArticleCount(String category, String search, Integer userId) {
        if (!categories.contains(category))
            category = null;
        return contentMapper.count("article", category, null, search, true, userId, "normal");
    }

    public List<Content> getArticleList(String category, String search, Integer userId, Integer beg, Integer num) {
        if (category != null && !categories.contains(category))
            category = null;
        return contentMapper.getContentList("article", category, null, search, true, userId, beg, num, "normal");
    }

    public Content getArticle(Integer id) {
        var ret = contentMapper.getContentById(id);
        return ret != null && ret.getObject().equals("article") ? ret : null;
    }

    public void delArticle(Integer id) {
        if (contentMapper.getContentById(id).getObject().equals("article"))
            contentMapper.setContentStatus(id, "delete");
    }
}
