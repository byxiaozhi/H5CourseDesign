package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.Comment;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.CommentService;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.util.Common;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    CommentService commentService;
    UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @RequestMapping("/api/comment/get")
    @ResponseBody
    public Map<String, Object> CommentGetApi(String object, Integer objectId, Integer userId, Integer page) {
        Integer beg = 10 * (page - 1), num = 10;
        var ret = new HashMap<>();
        var comment = new ArrayList<>();
        var count = commentService.count(object, objectId, userId);
        var maxPage = Math.max((int) Math.ceil(1.0 * count / num), 1);
        for (var t : commentService.getCommentList(object, objectId, userId, beg, num)) {
            var user = userService.getUserById(t.getUserId());
            comment.add(new HashMap<>() {{
                put("nickname", user.getNickname());
                put("time", Common.formatTime(t.getCreateTime()));
                put("content", t.getContent());
                put("object", t.getObject());
                put("objectId", t.getObjectId());
            }});
        }
        ret.put("pages", Common.makePagination(page, maxPage));
        ret.put("nowPage", page);
        ret.put("maxPage", maxPage);
        ret.put("count", count);
        ret.put("comment", comment);
        return Common.standardResp(0, null, ret);
    }

    @RequestMapping(value = "/api/comment/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> CommentAddApi(HttpSession session, @RequestBody JSONObject jsonObject) {
        if (!Common.objectList.contains(jsonObject.getString("object")))
            return Common.standardResp(1, "对象不存在", null);
        User user = (User) session.getAttribute("user");
        if (jsonObject.getString("content").trim().equals(""))
            return Common.standardResp(1, "内容不能为空", null);
        Comment comment = new Comment() {{
            setUserId(user.getId());
            setFatherId(-1);
            setObject(jsonObject.getString("object"));
            setObjectId(jsonObject.getInteger("objectId"));
            setContent(jsonObject.getString("content"));
            setCreateTime(Common.getNowTime());
            setStatus("normal");
            setExtra(new JSONObject().toJSONString());
        }};
        commentService.addComment(comment);
        return Common.standardResp(0, null, null);
    }
}
