package com.jluslda.edu.service;

import com.jluslda.edu.mapper.CategoryMapper;
import com.jluslda.edu.mapper.ContentMapper;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.util.Common;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocumentService {

    CategoryMapper categoryMapper;
    ContentMapper contentMapper;
    List<String> categories;
    static final Map<String, Map<String, Object>> documentViewCache = new HashMap<>();

    public DocumentService(CategoryMapper categoryMapper, ContentMapper contentMapper) {
        this.categoryMapper = categoryMapper;
        this.contentMapper = contentMapper;
        categories = categoryMapper.getCategory("document");
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addDocument(Content content) {
        content.setObject("document");
        contentMapper.addContent(content);
    }

    public Integer getDocumentCount(String category, String search, Integer userId) {
        if (category != null && !categories.contains(category))
            category = null;
        return contentMapper.count("document", category, null, search, false, userId, "normal");
    }

    public List<Content> getDocumentList(String category, String search, Integer userId, Integer beg, Integer num) {
        if (!categories.contains(category))
            category = null;
        return contentMapper.getContentList("document", category, null, search, false, userId, beg, num, "normal");
    }

    public Content getDocument(Integer id) {
        var ret = contentMapper.getContentById(id);
        return ret != null && ret.getObject().equals("document") ? ret : null;
    }

    public void delDocument(Integer id) {
        if (contentMapper.getContentById(id).getObject().equals("document"))
            contentMapper.setContentStatus(id, "delete");
    }

    public String getDocumentView(String resUrl) {
        if (resUrl.endsWith(".pdf"))
            return "https://media.ownfiles.cn/pdfjs/web/viewer.html?file=" + resUrl;
        synchronized (documentViewCache) {
            var cache = documentViewCache.get(resUrl);
            var time = new Date().getTime();
            if (cache == null || time > (long) cache.get("expired")) {
                cache = new HashMap<String, Object>();
                var officeUrl = "https://view.officeapps.live.com/op/embed.aspx?src=" + resUrl;
                for (int i = 0; i < 5; i++) {
                    try {
                        var responseBody = new RestTemplate().getForEntity(officeUrl, String.class).getBody();
                        System.out.println(responseBody);
                        var viewUrl = Common.unicodeDecode(Objects.requireNonNull(responseBody));
                        viewUrl = viewUrl.substring(viewUrl.indexOf("https://"));
                        viewUrl = viewUrl.substring(0, viewUrl.indexOf("'"));
                        viewUrl = viewUrl.replace("en%2DUS", "zh%2DCN");
                        if (new RestTemplate().getForEntity(viewUrl, String.class).getStatusCode().is2xxSuccessful()) {
                            cache.put("viewUrl", viewUrl);
                            cache.put("expired", new Date().getTime() + 60 * 60 * 1000);
                            documentViewCache.put(resUrl, cache);
                            break;
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return (String) cache.get("viewUrl");
        }
    }
}
