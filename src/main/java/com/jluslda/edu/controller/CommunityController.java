package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.CommentService;
import com.jluslda.edu.service.CommunityService;
import com.jluslda.edu.service.FavoriteService;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommunityController {

    UserService userService;
    CommunityService communityService;
    CommentService commentService;
    FavoriteService favoriteService;
    List<String> categories, tags;

    public CommunityController(UserService userService, CommunityService communityService, CommentService commentService, FavoriteService favoriteService) {
        this.userService = userService;
        this.communityService = communityService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
        categories = communityService.getCategories();
        tags = communityService.getTags();
    }

    @RequestMapping("/community")
    String community(Model model, String category, String tag, String search) {
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("search", search);
        model.addAttribute("category", category == null || !categories.contains(category) ? "全部" : category);
        model.addAttribute("tag", tag == null || !tags.contains(tag) ? "全部" : tag);
        return "community/index";
    }

    @RequestMapping("/community/view")
    String communityView(Model model, Integer id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var talk = communityService.getTalk(id);
        if (talk == null || !talk.getObject().equals("community") || !talk.getStatus().equals("normal"))
            return "404";
        var author = userService.getUserById(talk.getUserId());
        model.addAttribute("userid", talk.getUserId());
        model.addAttribute("email", author.getEmail());
        model.addAttribute("nickname", author.getNickname());
        model.addAttribute("id", talk.getId());
        model.addAttribute("title", talk.getTitle());
        model.addAttribute("commentCount", commentService.count("community", talk.getId(), null));
        model.addAttribute("content", talk.getContent());
        model.addAttribute("createTime", Common.formatTime(talk.getCreateTime()));
        model.addAttribute("categories", categories);
        model.addAttribute("isFavorite", user != null && favoriteService.isFavorite(user.getId(), "community", talk.getId()));
        model.addAttribute("tags", tags);
        return "community/view";
    }

    @RequestMapping("/community/create")
    String communityCreate(Model model) {
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        return "community/create";
    }

    @RequestMapping(value = "/api/community/create", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> communityCreateAPI(@RequestBody JSONObject params, HttpSession session) throws Exception {
        var title = params.getString("title");
        var category = params.getString("category");
        var content = Common.htmlFilter(params.getString("content"));
        var talkTags = params.getJSONArray("tags").toJavaList(String.class);
        if (!categories.contains(category))
            return Common.standardResp(1, "请选择板块", null);
        if (title.length() < 4)
            return Common.standardResp(1, "标题长度最少为4个字符", null);
        if (content.length() < 10)
            return Common.standardResp(1, "内容太短", null);
        if (talkTags.size() == 0)
            return Common.standardResp(1, "最少需要一个标签", null);
        if (talkTags.contains(""))
            return Common.standardResp(1, "不能有空的标签", null);
        User user = (User) session.getAttribute("user");
        communityService.addTalk(new Content() {{
            setUserId(user.getId());
            setCategory(category);
            setTitle(title);
            setContent(content);
        }}, talkTags);

        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/community/getTalkList")
    @ResponseBody
    Map<String, Object> getTalkListApi(String category, String tag, String search, Integer userId, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        var map = new HashMap<String, Object>();
        Integer beg = 10 * (page - 1), num = 10;
        var count = communityService.getTalkCount(category, tag, search, userId);
        var talks = new ArrayList<Map<String, Object>>();
        var maxPage = Math.max((int) Math.ceil(1.0 * count / num), 1);
        map.put("count", count);
        map.put("pages", Common.makePagination(page, maxPage));
        map.put("nowPage", page);
        map.put("maxPage", maxPage);
        for (var item : communityService.getTalkList(category, tag, search, userId, beg, num)) {
            talks.add(new HashMap<>() {{
                var user = userService.getUserById(item.getUserId());
                var content = Common.htmlClean(item.getContent());
                put("userid", item.getUserId());
                put("nickname", user.getNickname());
                put("id", item.getId());
                put("title", item.getTitle());
                put("commentCount", commentService.count("community", item.getId(), null));
                put("content", content.length() > 120 ? content.substring(0, 120) + "..." : content);
                put("createTime", Common.formatTime(item.getCreateTime()));
            }});
        }
        map.put("talks", talks);
        return Common.standardResp(0, null, map);
    }

    @RequestMapping(value = "/api/community/deleteTalk", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> deleteTalkApi(@RequestBody JSONObject jsonObject, HttpSession session) {
        var id = jsonObject.getInteger("id");
        var talk = communityService.getTalk(id);
        var user = (User) session.getAttribute("user");
        if (!talk.getUserId().equals(user.getId()))
            return Common.standardResp(1, "无权限", null);
        communityService.delTalk(id);
        return Common.standardResp(0, null, null);
    }
}
