package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.ArticleService;
import com.jluslda.edu.service.CommentService;
import com.jluslda.edu.service.FavoriteService;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.util.Common;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController {

    ArticleService articleService;
    UserService userService;
    CommentService commentService;
    FavoriteService favoriteService;
    List<String> categories;

    public ArticleController(ArticleService articleService, UserService userService, CommentService commentService, FavoriteService favoriteService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
        this.categories = articleService.getCategories();
    }

    @RequestMapping("/article")
    String article(Model model, String category, String search) {
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("category", category == null || !categories.contains(category) ? "全部" : category);
        return "article/index";
    }

    @RequestMapping("/article/view")
    String articleView(Model model, Integer id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var article = articleService.getArticle(id);
        if (article == null || !article.getObject().equals("article") || !article.getStatus().equals("normal"))
            return "404";
        var author = userService.getUserById(article.getUserId());
        model.addAttribute("userid", article.getUserId());
        model.addAttribute("email", author.getEmail());
        model.addAttribute("nickname", author.getNickname());
        model.addAttribute("id", article.getId());
        model.addAttribute("title", article.getTitle());
        model.addAttribute("commentCount", commentService.count("article", article.getId(), null));
        model.addAttribute("content", article.getContent());
        model.addAttribute("createTime", Common.formatTime(article.getCreateTime()));
        model.addAttribute("categories", categories);
        model.addAttribute("isFavorite", user != null && favoriteService.isFavorite(user.getId(), "article", article.getId()));
        return "article/view";
    }

    @RequestMapping("/article/create")
    String articleCreate(Model model) {
        model.addAttribute("categories", categories);
        return "article/create";
    }

    @RequestMapping(value = "/api/article/create", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> articleCreateAPI(@RequestBody JSONObject params, HttpSession session) throws Exception {
        var title = params.getString("title");
        var category = params.getString("category");
        var description = params.getString("description");
        var cover = params.getString("cover");
        var content = Common.htmlFilter(params.getString("content"));

        if (!categories.contains(category))
            return Common.standardResp(1, "请选择分类", null);
        if (title.length() < 4)
            return Common.standardResp(1, "标题长度最少为4个字符", null);
        if (description.length() < 4)
            return Common.standardResp(1, "简介长度最少为4个字符", null);
        if (description.length() > 300)
            return Common.standardResp(1, "简介长度最多为300个字符", null);
        if (content.length() < 10)
            return Common.standardResp(1, "内容太短", null);
        if (!cover.startsWith("https://"))
            return Common.standardResp(1, "请选择封面图片", null);
        User user = (User) session.getAttribute("user");
        articleService.addArticle(new Content() {{
            setUserId(user.getId());
            setCategory(category);
            setTitle(title);
            setCover(cover);
            setDescription(description);
            setContent(content);
        }});

        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/article/getArticleList")
    @ResponseBody
    Map<String, Object> getArticleListApi(String category, String search, Integer userId, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        var map = new HashMap<String, Object>();
        Integer beg = 10 * (page - 1), num = 10;
        var count = articleService.getArticleCount(category, search, userId);
        var articles = new ArrayList<Map<String, Object>>();
        var maxPage = Math.max((int) Math.ceil(1.0 * count / num), 1);
        map.put("count", count);
        map.put("pages", Common.makePagination(page, maxPage));
        map.put("nowPage", page);
        map.put("maxPage", maxPage);
        for (var item : articleService.getArticleList(category, search, userId, beg, num)) {
            articles.add(new HashMap<>() {{
                var user = userService.getUserById(item.getUserId());
                put("userid", item.getUserId());
                put("nickname", user.getNickname());
                put("id", item.getId());
                put("title", item.getTitle());
                put("cover", item.getCover());
                put("commentCount", commentService.count("article", item.getId(), null));
                put("description", item.getDescription());
                put("createTime", Common.formatTime(item.getCreateTime()));
            }});
        }
        map.put("articles", articles);
        return Common.standardResp(0, null, map);
    }

    @RequestMapping(value = "/api/article/deleteArticle", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> deleteArticleApi(@RequestBody JSONObject jsonObject, HttpSession session) {
        var id = jsonObject.getInteger("id");
        var article = articleService.getArticle(id);
        var user = (User) session.getAttribute("user");
        if (!article.getUserId().equals(user.getId()))
            return Common.standardResp(1, "无权限", null);
        articleService.delArticle(id);
        return Common.standardResp(0, null, null);
    }
}
