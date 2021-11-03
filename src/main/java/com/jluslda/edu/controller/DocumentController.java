package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.CommentService;
import com.jluslda.edu.service.DocumentService;
import com.jluslda.edu.service.FavoriteService;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DocumentController {

    DocumentService documentService;
    UserService userService;
    CommentService commentService;
    FavoriteService favoriteService;
    List<String> categories;

    public DocumentController(DocumentService documentService, UserService userService, CommentService commentService, FavoriteService favoriteService) {
        this.documentService = documentService;
        this.userService = userService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
        this.categories = documentService.getCategories();
    }

    @RequestMapping("/document")
    String document(Model model, String category, String search) {
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("category", category == null || !categories.contains(category) ? "全部" : category);
        return "document/index";
    }

    @RequestMapping("/document/view")
    String documentView(Model model, Integer id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var document = documentService.getDocument(id);
        if (document == null || !document.getObject().equals("document") || !document.getStatus().equals("normal"))
            return "404";
        var author = userService.getUserById(document.getUserId());
        model.addAttribute("userid", document.getUserId());
        model.addAttribute("email", author.getEmail());
        model.addAttribute("nickname", author.getNickname());
        model.addAttribute("id", document.getId());
        model.addAttribute("title", document.getTitle());
        model.addAttribute("description", document.getDescription());
        model.addAttribute("commentCount", commentService.count("document", document.getId(), null));
        model.addAttribute("createTime", Common.formatTime(document.getCreateTime()));
        model.addAttribute("categories", categories);
        model.addAttribute("isFavorite", user != null && favoriteService.isFavorite(user.getId(), "document", document.getId()));
        return "document/view";
    }

    @RequestMapping("/api/document/getDocumentViewUrl")
    @ResponseBody
    Map<String, Object> getDocumentViewUrl(Integer id) {
        var document = documentService.getDocument(id);
        if (document == null || !document.getObject().equals("document"))
            return Common.standardResp(1, "加载失败，请稍后再试", null);
        var url = documentService.getDocumentView(document.getContent());
        if (url == null)
            return Common.standardResp(1, "加载失败，请稍后再试", null);
        else
            return Common.standardResp(0, null, url);
    }

    @RequestMapping("/document/create")
    String documentCreate(Model model) {
        model.addAttribute("categories", categories);
        return "document/create";
    }

    @RequestMapping(value = "/api/document/create", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> documentCreateAPI(@RequestBody JSONObject params, HttpSession session) throws Exception {
        var title = params.getString("title");
        var category = params.getString("category");
        var description = params.getString("description");
        var cover = params.getString("cover");
        var content = params.getString("content");

        if (!categories.contains(category))
            return Common.standardResp(1, "请选择分类", null);
        if (title.length() < 4)
            return Common.standardResp(1, "标题长度最少为4个字符", null);
        if (description.length() < 4)
            return Common.standardResp(1, "简介长度最少为4个字符", null);
        if (description.length() > 300)
            return Common.standardResp(1, "简介长度最多为300个字符", null);
        if (!content.startsWith("https://"))
            return Common.standardResp(1, "请上传文档", null);
        if (!cover.startsWith("https://"))
            return Common.standardResp(1, "请选择封面图片", null);
        User user = (User) session.getAttribute("user");
        documentService.addDocument(new Content() {{
            setUserId(user.getId());
            setCategory(category);
            setTitle(title);
            setCover(cover);
            setDescription(description);
            setContent(content);
        }});

        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/document/getDocumentList")
    @ResponseBody
    Map<String, Object> getDocumentListApi(String category, String search, Integer userId, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        var map = new HashMap<String, Object>();
        Integer beg = 10 * (page - 1), num = 10;
        var count = documentService.getDocumentCount(category, search, userId);
        var documents = new ArrayList<Map<String, Object>>();
        var maxPage = Math.max((int) Math.ceil(1.0 * count / num), 1);
        map.put("count", count);
        map.put("pages", Common.makePagination(page, maxPage));
        map.put("nowPage", page);
        map.put("maxPage", maxPage);
        for (var item : documentService.getDocumentList(category, search, userId, beg, num)) {
            documents.add(new HashMap<>() {{
                var user = userService.getUserById(item.getUserId());
                put("userid", item.getUserId());
                put("nickname", user.getNickname());
                put("id", item.getId());
                put("title", item.getTitle());
                put("cover", item.getCover());
                put("commentCount", commentService.count("documents", item.getId(), null));
                put("description", item.getDescription());
                put("createTime", Common.formatTime(item.getCreateTime()));
            }});
        }
        map.put("documents", documents);
        return Common.standardResp(0, null, map);
    }

    @RequestMapping(value = "/api/document/deleteDocument", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> deleteDocumentApi(@RequestBody JSONObject jsonObject, HttpSession session) {
        var id = jsonObject.getInteger("id");
        var document = documentService.getDocument(id);
        var user = (User) session.getAttribute("user");
        if (!document.getUserId().equals(user.getId()))
            return Common.standardResp(1, "无权限", null);
        documentService.delDocument(id);
        return Common.standardResp(0, null, null);
    }

}
