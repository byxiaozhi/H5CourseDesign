package com.jluslda.edu.service;

import com.jluslda.edu.mapper.ContentMapper;
import com.jluslda.edu.model.Content;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    ContentMapper contentMapper;

    public ContentService(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    public Content getContentById(Integer id) {
        return contentMapper.getContentById(id);
    }

}
