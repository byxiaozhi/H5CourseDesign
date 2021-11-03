package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.*;
import com.jluslda.edu.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    UserService userService;
    FollowService followService;
    FavoriteService favoriteService;
    ContentService contentService;
    LetterService letterService;

    public UserController(UserService userService, FollowService followService, ContentService contentService, LetterService letterService, FavoriteService favoriteService) {
        this.userService = userService;
        this.followService = followService;
        this.letterService = letterService;
        this.contentService = contentService;
        this.favoriteService = favoriteService;
    }

    @RequestMapping("/signin")
    String signinPage(HttpServletResponse response, HttpSession session) {
        return "account/signin";
    }

    @RequestMapping("/signup")
    String signupPage(HttpServletResponse response, HttpSession session) {
        return "account/signup";
    }

    @RequestMapping("/forget")
    String forgetPage(HttpServletResponse response, HttpSession session) {
        return "account/forget";
    }

    @RequestMapping("/avatar")
    void accountAvatar(String email, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("https://sdn.geekzu.org/avatar/%s", DigestUtils.md5DigestAsHex(email.getBytes())));
    }

    @RequestMapping("/account/overview")
    String accountOverview(Integer uid, Model model, HttpServletResponse response, HttpSession session) {
        model.addAttribute("user", userService.getUserById(uid));
        return "account/overview";
    }

    @RequestMapping("/account/{page}")
    String accountPage(Model model, @PathVariable("page") String page, HttpServletResponse response, HttpSession session) {
        model.addAttribute("page", page);
        return "account/main";
    }

    @RequestMapping("/signout")
    @ResponseBody
    void signout(HttpServletResponse response, HttpSession session) throws IOException {
        session.removeAttribute("user");
        response.addCookie(new Cookie("email", null) {{
            setMaxAge(0);
        }});
        response.addCookie(new Cookie("password", null) {{
            setMaxAge(0);
        }});
        response.sendRedirect("/");
    }

    @RequestMapping(value = "/api/signin", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> signinAPI(@RequestBody Map<String, String> params, HttpSession session, HttpServletResponse response) {
        String email = params.get("email");
        String password = Common.pwdEncrypt(params.get("password"));
        boolean autoSignin = Boolean.parseBoolean(params.get("autoSignin"));
        User user = userService.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            userService.updateTime(user.getId());
            if (autoSignin) {
                response.addCookie(new Cookie("email", email) {{
                    setMaxAge(2592000);
                    setPath("/");
                }});
                response.addCookie(new Cookie("password", password) {{
                    setMaxAge(2592000);
                    setPath("/");
                }});
            }
            return Common.standardResp(0, null, null);
        } else {
            return Common.standardResp(1, "用户名或密码错误", null);
        }
    }

    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> signupAPI(@Valid @RequestBody User user, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            var fieldError = bindingResult.getFieldError();
            String err = fieldError == null ? null : fieldError.getDefaultMessage();
            return Common.standardResp(1, err, null);
        }
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return Common.standardResp(1, "邮箱已经存在", null);
        }
        user.setPassword(Common.pwdEncrypt(user.getPassword()));
        user.setSignature("");
        user.setExtra(new JSONObject().toJSONString());
        user.setStatus("normal");
        user.setCreateTime(Common.getNowTime());
        user.setUpdateTime(Common.getNowTime());
        userService.addUser(user);
        session.setAttribute("user", user);
        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/account/overview")
    @ResponseBody
    Map<String, Object> signupAPI(Integer uid, HttpSession session) {
        var user0 = (User) session.getAttribute("user");
        var user1 = userService.getUserById(uid);
        if (user1 == null)
            return Common.standardResp(1, "用户不存在", null);
        return Common.standardResp(0, null, new HashMap<>() {{
            put("nickname", user1.getNickname());
            put("isFollow", followService.isFollow(user0.getId(), user1.getId()));
        }});
    }

    @RequestMapping(value = "/api/account/follow", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> followAPI(@RequestBody JSONObject jsonObject, HttpSession session) {
        var user0 = (User) session.getAttribute("user");
        var user1 = userService.getUserById(jsonObject.getInteger("uid"));
        if (jsonObject.getBoolean("follow") && !followService.isFollow(user0.getId(), user1.getId()))
            followService.follow(user0.getId(), user1.getId());
        else if (!jsonObject.getBoolean("follow") && followService.isFollow(user0.getId(), user1.getId()))
            followService.unFollow(user0.getId(), user1.getId());
        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/account/getInformation")
    @ResponseBody
    Map<String, Object> signupAPI(HttpSession session) {
        var user = (User) session.getAttribute("user");
        var map = new HashMap<>();
        map.put("nickname", user.getNickname());
        map.put("email", user.getEmail());
        map.put("signature", user.getSignature());
        return Common.standardResp(0, null, map);
    }

    @RequestMapping(value = "/api/account/setInformation", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> setInformationAPI(HttpSession session, @RequestBody JSONObject jsonObject) {
        var user = (User) session.getAttribute("user");
        var nickname = jsonObject.getString("nickname").trim();
        if (nickname.length() < 2 || nickname.length() > 64)
            return Common.standardResp(1, "昵称长度需要在2到64之间", null);
        user.setSignature(jsonObject.getString("signature").trim());
        user.setNickname(nickname);
        userService.updateUser(user);
        return Common.standardResp(0, null, null);
    }

    @RequestMapping(value = "/api/account/changePassword", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> changePasswordAPI(HttpSession session, @RequestBody JSONObject jsonObject) {
        var user = (User) session.getAttribute("user");
        var passwordOld = jsonObject.getString("passwordOld").trim();
        var passwordNew = jsonObject.getString("passwordNew").trim();
        var passwordNew2 = jsonObject.getString("passwordNew2").trim();
        if (!passwordNew.equals(passwordNew2))
            return Common.standardResp(1, "确认密码不一致", null);
        if (passwordNew.length() < 6 || passwordNew.length() > 64)
            return Common.standardResp(1, "密码长度需要在6到64之间", null);
        if (!user.getPassword().equals(Common.pwdEncrypt(passwordOld)))
            return Common.standardResp(1, "原密码不匹配", null);
        user.setPassword(Common.pwdEncrypt(passwordNew));
        userService.updateUser(user);
        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/account/getLetterList")
    @ResponseBody
    Map<String, Object> getLetterListAPI(HttpSession session, Integer startId) {
        var user = (User) session.getAttribute("user");
        var letterList = letterService.getLetterList(user.getId(), startId);
        var nicknameMap = new HashMap<Integer, String>();
        var ret = new ArrayList<>();
        for (var t : letterList) {
            String fromUserNickname, toUserNickname;
            if (nicknameMap.containsKey(t.getFromUserId())) {
                fromUserNickname = nicknameMap.get(t.getFromUserId());
            } else {
                fromUserNickname = userService.getUserById(t.getFromUserId()).getNickname();
                nicknameMap.put(t.getFromUserId(), fromUserNickname);
            }
            if (nicknameMap.containsKey(t.getToUserId())) {
                toUserNickname = nicknameMap.get(t.getToUserId());
            } else {
                toUserNickname = userService.getUserById(t.getToUserId()).getNickname();
                nicknameMap.put(t.getToUserId(), toUserNickname);
            }
            ret.add(new HashMap<>() {{
                put("id", t.getId());
                put("fromUserId", t.getFromUserId());
                put("toUserId", t.getToUserId());
                put("fromUserNickname", fromUserNickname);
                put("toUserNickname", toUserNickname);
                put("content", t.getContent());
            }});
        }
        return Common.standardResp(0, null, ret);
    }

    @RequestMapping(value = "/api/account/sendLetterList", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> sendLetterAPI(HttpSession session, @RequestBody JSONObject params) {
        var content = params.getString("content").trim();
        var toUserId = params.getInteger("toUserId");
        var user = (User) session.getAttribute("user");
        var toUser = userService.getUserById(toUserId);
        if (toUser == null)
            return Common.standardResp(1, "用户不存在", "");
        if (content.equals(""))
            return Common.standardResp(1, "发送内容不能为空", "");
        letterService.sendLetter(user.getId(), toUser.getId(), content);
        return Common.standardResp(0, "发送成功", "");
    }

    @RequestMapping("/api/account/getFollow")
    @ResponseBody
    Map<String, Object> getFollowAPI(HttpSession session, Integer type, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Integer beg = 10 * (page - 1), num = 10; // 来不及写分页了，以后加上
        var user = (User) session.getAttribute("user");
        var follow = new ArrayList<>();
        var list = type == 1 ?
                followService.getFollowMe(user.getId(), 0, 1000) :
                followService.getMyFollow(user.getId(), 0, 1000);
        for (var t : list) {
            follow.add(new HashMap<>(){{
                put("id", t.getId());
                put("fromUserId", t.getFromUserId());
                put("toUserId", t.getToUserId());
                put("fromUserNickname", userService.getUserById(t.getFromUserId()).getNickname());
                put("toUserNickname", userService.getUserById(t.getToUserId()).getNickname());
            }});
        }
        return Common.standardResp(0, null, follow);
    }

    @RequestMapping("/api/account/setFavorite")
    @ResponseBody
    Map<String, Object> setFavoriteAPI(HttpSession session, @RequestBody JSONObject jsonObject) {
        var favorite = jsonObject.getBoolean("favorite");
        var userId = ((User)session.getAttribute("user")).getId();
        var objectId = jsonObject.getInteger("objectId");
        var object = contentService.getContentById(objectId).getObject();
        if(favorite)
            favoriteService.setFavorite(userId, object, objectId);
        else
            favoriteService.unsetFavorite(userId, object, objectId);
        return Common.standardResp(0, null, favoriteService.isFavorite(userId, object, objectId));
    }

    @RequestMapping("/api/account/getFavorites")
    @ResponseBody
    Map<String, Object> getFavoritesAPI(HttpSession session, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Integer beg = 10 * (page - 1), num = 10; // 来不及写分页了，以后加上
        var userId = ((User)session.getAttribute("user")).getId();
        var ret = new ArrayList<>();
        for(var t : favoriteService.getFavorites(userId,0, 1000)){
            ret.add(new HashMap<>(){{
                var content = contentService.getContentById(t.getObjectId());
                var user = userService.getUserById(content.getUserId());
                put("object", t.getObject());
                put("objectId", t.getObjectId());
                put("title", content.getTitle());
                put("author", user.getNickname());
                put("authorId", user.getId());
            }});
        };
        return Common.standardResp(0, null, ret);
    }


}
