package com.jluslda.edu.controller;

import com.jluslda.edu.model.User;
import com.jluslda.edu.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@Controller
public class MainController {

    String uploadPath = System.getProperty("user.dir") + File.separator + "upload" + File.separator;

    @RequestMapping("/")
    String index(Model model) {
        return "index";
    }

    @RequestMapping("/about")
    String about(Model model) {
        return "about";
    }

    /*
    @RequestMapping("/api/imageUpload")
    @ResponseBody
    Map<String, Object> imageUpload(@RequestParam("file") MultipartFile file, HttpSession session) throws Exception {
        var user = (User) session.getAttribute("user");
        if (user == null)
            return Common.standardResp(1, "请先登录", null);
        if (file.isEmpty())
            return Common.standardResp(1, "文件为空", null);
        String filename = Common.getRandomString(16);
        var tmpFile = File.createTempFile(filename, "tmp");
        var output = new File(uploadPath + filename + ".jpeg");
        file.transferTo(tmpFile);
        ImageIO.write(ImageIO.read(tmpFile), "jpeg", output);
        tmpFile.deleteOnExit();
        return Common.standardResp(0, "", "/upload/" + filename + ".jpeg");
    }

    @RequestMapping("/upload/{filename}")
    @ResponseBody
    void getUploadFile(@PathVariable("filename") String filename, HttpServletResponse response) throws Exception {
        var file = new File(uploadPath + filename);
        var in = new FileInputStream(file);
        var out = response.getOutputStream();
        response.setContentType("image/jpeg");
        response.setContentLength((int) file.length());
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);
        while (len != -1) {
            out.write(buffer, 0, len);
            len = in.read(buffer);
        }
        out.flush();
        out.close();
    }
     */

}
