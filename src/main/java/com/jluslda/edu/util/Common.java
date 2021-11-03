package com.jluslda.edu.util;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Common {

    static Random random = new Random();

    public static List<String> objectList = List.of("community", "article", "video", "document");

    public static String pwdEncrypt(String pwd) {
        return md5(pwd);
    }

    public static String md5(String content) {
        return DigestUtils.md5DigestAsHex(content.getBytes());
    }

    public static Map<String, Object> standardResp(int code, String msg, Object data) {
        return new HashMap<>() {{
            put("code", code);
            put("msg", msg);
            put("data", data);
        }};
    }

    public static List<Integer> makePagination(Integer nowPage, Integer maxPage) {
        Integer displayStartPage = 0, displayEndPage = 0;
        if (nowPage < maxPage / 2) {
            displayStartPage = Math.max(1, nowPage - 2);
            displayEndPage = Math.min(maxPage, displayStartPage + 4);
        } else {
            displayEndPage = Math.min(maxPage, nowPage + 2);
            displayStartPage = Math.max(1, displayEndPage - 4);
        }
        return IntStream.rangeClosed(displayStartPage, displayEndPage).boxed().collect(Collectors.toList());
    }

    public static String formatTime(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy/MM/dd").format(timestamp);
    }

    public static Timestamp getNowTime() {
        return new Timestamp(new java.util.Date().getTime());
    }

    public static String htmlFilter(String html) {
        Whitelist whitelist = Whitelist.relaxed();
        whitelist.removeProtocols("img", "src", "http", "https");
        whitelist.addAttributes(":all", "style", "class");
        return Jsoup.clean(html, whitelist);
    }

    public static String htmlClean(String html) {
        return Jsoup.clean(html, Whitelist.none());
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(str.charAt(random.nextInt(62)));
        return sb.toString();
    }

    public static String unicodeDecode(String unicode) {
        String[] strArray = unicode.split("\\\\u");
        StringBuilder ret = new StringBuilder();
        ret.append(strArray[0]);
        for (int i = 1; i < strArray.length; i++) {
            ret.append((char) Integer.valueOf(strArray[i].substring(0, 4), 16).intValue());
            ret.append(strArray[i].substring(4));
        }
        return ret.toString();
    }

    public static void setResponse(Map<String, Object> content, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = new JSONObject(content);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.getOutputStream().write(jsonObject.toJSONString().getBytes());
    }
}
