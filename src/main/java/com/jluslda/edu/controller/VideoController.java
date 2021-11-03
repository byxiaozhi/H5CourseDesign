package com.jluslda.edu.controller;

import com.alibaba.fastjson.JSONObject;
import com.jluslda.edu.model.Content;
import com.jluslda.edu.model.User;
import com.jluslda.edu.service.CommentService;
import com.jluslda.edu.service.FavoriteService;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.service.VideoService;
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
public class VideoController {

    VideoService videoService;
    UserService userService;
    CommentService commentService;
    FavoriteService favoriteService;
    List<String> categories, availableCategories;

    public VideoController(VideoService videoService, UserService userService, CommentService commentService, FavoriteService favoriteService) {
        this.videoService = videoService;
        this.userService = userService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
        this.categories = videoService.getCategories();
        this.availableCategories = new ArrayList<>(this.categories); // 用户投稿不能选择直播
        this.availableCategories.remove("直播");
    }

    @RequestMapping("/video")
    String video(Model model, String category, String search) {
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("category", category == null || !categories.contains(category) ? "全部" : category);
        return "video/index";
    }

    @RequestMapping("/video/view")
    String videoView(Model model, Integer id, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var video = videoService.getVideo(id);
        if (video == null || !video.getObject().equals("video") || !video.getStatus().equals("normal"))
            return "404";
        var author = userService.getUserById(video.getUserId());
        model.addAttribute("userid", video.getUserId());
        model.addAttribute("email", author.getEmail());
        model.addAttribute("nickname", author.getNickname());
        model.addAttribute("id", video.getId());
        model.addAttribute("title", video.getTitle());
        model.addAttribute("description", video.getDescription());
        model.addAttribute("commentCount", commentService.count("video", video.getId(), null));
        model.addAttribute("videoUrl", video.getContent());
        model.addAttribute("createTime", Common.formatTime(video.getCreateTime()));
        model.addAttribute("categories", categories);
        model.addAttribute("isFavorite", user != null && favoriteService.isFavorite(user.getId(), "video", video.getId()));
        return "video/view";
    }

    @RequestMapping("/video/create")
    String videoCreate(Model model) {
        model.addAttribute("categories", availableCategories);
        return "video/create";
    }

    @RequestMapping(value = "/api/video/create", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> videoCreateAPI(@RequestBody JSONObject params, HttpSession session) throws Exception {
        var title = params.getString("title");
        var category = params.getString("category");
        var description = params.getString("description");
        var cover = params.getString("cover");
        var content = params.getString("content");

        if (!availableCategories.contains(category))
            return Common.standardResp(1, "请选择分类", null);
        if (title.length() < 4)
            return Common.standardResp(1, "标题长度最少为4个字符", null);
        if (description.length() < 4)
            return Common.standardResp(1, "简介长度最少为4个字符", null);
        if (description.length() > 300)
            return Common.standardResp(1, "简介长度最多为300个字符", null);
        if (!content.startsWith("https://"))
            return Common.standardResp(1, "请上传视频", null);
        if (!cover.startsWith("https://"))
            return Common.standardResp(1, "请选择封面图片", null);
        User user = (User) session.getAttribute("user");
        videoService.addVideo(new Content() {{
            setUserId(user.getId());
            setCategory(category);
            setTitle(title);
            setCover(cover);
            setDescription(description);
            setContent(content);
        }});

        return Common.standardResp(0, null, null);
    }

    @RequestMapping("/api/video/getVideoList")
    @ResponseBody
    Map<String, Object> getVideoListApi(String category, String search, Integer userId, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        var map = new HashMap<String, Object>();
        Integer beg = 10 * (page - 1), num = 10;
        var count = videoService.getVideoCount(category, search, userId);
        var videos = new ArrayList<Map<String, Object>>();
        var maxPage = Math.max((int) Math.ceil(1.0 * count / num), 1);
        map.put("count", count);
        map.put("pages", Common.makePagination(page, maxPage));
        map.put("nowPage", page);
        map.put("maxPage", maxPage);
        for (var item : videoService.getVideoList(category, search, userId, beg, num)) {
            videos.add(new HashMap<>() {{
                var user = userService.getUserById(item.getUserId());
                put("userid", item.getUserId());
                put("nickname", user.getNickname());
                put("id", item.getId());
                put("title", item.getTitle());
                put("cover", item.getCover());
                put("commentCount", commentService.count("video", item.getId(), null));
                put("description", item.getDescription());
                put("createTime", Common.formatTime(item.getCreateTime()));
            }});
        }
        map.put("videos", videos);
        return Common.standardResp(0, null, map);
    }

    @RequestMapping(value = "/api/video/deleteVideo", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> deleteVideoApi(@RequestBody JSONObject jsonObject, HttpSession session) {
        var id = jsonObject.getInteger("id");
        var video = videoService.getVideo(id);
        var user = (User) session.getAttribute("user");
        if (!video.getUserId().equals(user.getId()))
            return Common.standardResp(1, "无权限", null);
        videoService.delVideo(id);
        return Common.standardResp(0, null, null);
    }

    String getRtmpCode(Content content) {
        return Common.md5("" + content.getUserId() + content.getCreateTime().getTime()).substring(8, 24);
    }

    @RequestMapping(value = "/api/video/openLive", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> openLiveAPI(@RequestBody JSONObject params, HttpSession session) throws Exception {

        var title = params.getString("title");
        var description = params.getString("description");
        var cover = params.getString("cover");

        if (title.length() < 4)
            return Common.standardResp(1, "标题长度最少为4个字符", null);
        if (description.length() < 4)
            return Common.standardResp(1, "简介长度最少为4个字符", null);
        if (description.length() > 300)
            return Common.standardResp(1, "简介长度最多为300个字符", null);
        if (!cover.startsWith("https://"))
            return Common.standardResp(1, "请选择封面图片", null);
        User user = (User) session.getAttribute("user");
        if (videoService.getVideoList("直播", null, user.getId(), 0, 1).size() > 0) {
            return Common.standardResp(1, "不能重复开启直播间", null);
        }
        var video = new Content() {{
            setUserId(user.getId());
            setCategory("直播");
            setTitle(title);
            setCover(cover);
            setDescription(description);
        }};
        var rtmpCode = getRtmpCode(video);
        video.setContent("https://media.ownfiles.cn/live/" + rtmpCode + ".m3u8");
        video.setExtra("rtmpCode", rtmpCode);
        videoService.addVideo(video);
        return getLiveAPI(session);
    }

    @RequestMapping(value = "/api/video/closeLive")
    @ResponseBody
    Map<String, Object> CloseLiveAPI(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        var room = videoService.getVideoList("直播", null, user.getId(), 0, 1);
        if (room.size() == 0)
            return Common.standardResp(1, "还未开启直播间", null);
        for (var t : room)
            videoService.delVideo(t.getId());
        return Common.standardResp(0, null, null);
    }

    @RequestMapping(value = "/api/video/getLive")
    @ResponseBody
    Map<String, Object> getLiveAPI(HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        var room = videoService.getVideoList("直播", null, user.getId(), 0, 1);
        if (room.size() == 0)
            return Common.standardResp(1, "还未开启直播间", null);
        return Common.standardResp(0, null, new HashMap<>() {{
            put("roomId", room.get(0).getId());
            put("rtmp", "rtmp://live-push.ownbox.cn/live-bvc/" + room.get(0).getExtra("rtmpCode"));
        }});
    }


}
