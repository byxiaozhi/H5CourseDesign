package com.jluslda.edu.service;

import com.jluslda.edu.mapper.CategoryMapper;
import com.jluslda.edu.mapper.ContentMapper;
import com.jluslda.edu.model.Content;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    CategoryMapper categoryMapper;
    ContentMapper contentMapper;
    List<String> categories;

    public VideoService(CategoryMapper categoryMapper, ContentMapper contentMapper) {
        this.categoryMapper = categoryMapper;
        this.contentMapper = contentMapper;
        categories = categoryMapper.getCategory("video");
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addVideo(Content content) {
        content.setObject("video");
        contentMapper.addContent(content);
    }

    public Integer getVideoCount(String category, String search, Integer userId) {
        if (!categories.contains(category))
            category = null;
        return contentMapper.count("video", category, null, search, false, userId, "normal");
    }

    public List<Content> getVideoList(String category, String search, Integer userId, Integer beg, Integer num) {
        if (category != null && !categories.contains(category) && !category.equals("直播"))
            category = null;
        return contentMapper.getContentList("video", category, null, search, false, userId, beg, num, "normal");
    }

    public Content getVideo(Integer id) {
        var ret = contentMapper.getContentById(id);
        return ret != null && ret.getObject().equals("video") ? ret : null;
    }

    public void delVideo(Integer id) {
        if (contentMapper.getContentById(id).getObject().equals("video"))
            contentMapper.setContentStatus(id, "delete");
    }
}
