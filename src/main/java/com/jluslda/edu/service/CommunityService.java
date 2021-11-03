package com.jluslda.edu.service;

import com.jluslda.edu.mapper.CategoryMapper;
import com.jluslda.edu.mapper.TagMapper;
import com.jluslda.edu.mapper.ContentMapper;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    CategoryMapper categoryMapper;
    TagMapper tagMapper;
    ContentMapper contentMapper;
    UserService userService;
    List<String> categories, tags;

    public CommunityService(CategoryMapper categoryMapper, TagMapper tagMapper, ContentMapper contentMapper, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.contentMapper = contentMapper;
        this.userService = userService;
        categories = categoryMapper.getCategory("community");
        tags = tagMapper.getTag("community", 10);
    }

    public List<Content> getTalkList(String category, String tag, String search, Integer userId, Integer beg, Integer num) {
        if (category != null && !categories.contains(category))
            category = null;
        if (tag != null && !tags.contains(tag))
            tag = null;
        return contentMapper.getContentList("community", category, tag, search, true, userId, beg, num, "normal");
    }

    public Content getTalk(Integer id) {
        var ret = contentMapper.getContentById(id);
        return ret != null && ret.getObject().equals("community") ? ret : null;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public Integer getTalkCount(String category, String tag, String search, Integer userId) {
        if (!categories.contains(category))
            category = null;
        if (!tags.contains(tag))
            tag = null;
        return contentMapper.count("community", category, tag, search, true, userId, "normal");
    }

    public void addTalk(Content content, List<String> talkTags) {
        content.setObject("community");
        contentMapper.addContent(content);
        for (var t : talkTags) {
            tagMapper.addTag(new Tag() {{
                setObject("community");
                setObjectId(content.getId());
                setName(t);
            }});
        }
    }

    public void delTalk(Integer id) {
        if (contentMapper.getContentById(id).getObject().equals("community"))
            contentMapper.setContentStatus(id, "delete");
    }
}
