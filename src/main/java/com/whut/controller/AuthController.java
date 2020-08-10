package com.whut.controller;

import com.whut.aop.interfaces.CheckPermission;
import com.whut.aop.interfaces.RecordLog;
import com.whut.dao.UserRepository;
import com.whut.dao.entity.FaceInfo;
import com.whut.dao.entity.User;
import com.whut.domain.Response;
import com.whut.service.FaceRecognitionService;
import com.whut.util.CookieUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆
 *
 * @author yy
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FaceRecognitionService faceRecognitionService;


    @PostMapping(value = "/register")
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String sex = request.getParameter("sex");
        String department = request.getParameter("department");
        String roleId = request.getParameter("role_id");
        String base64Image = request.getParameter("base64_image");
        if (username == null || base64Image == null || name == null || phone == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        base64Image = base64Image.replaceAll(" ","+");
        if (!faceRecognitionService.saveFaceInfo(username, base64Image)){
            return new Response(Response.Code.ImageError).toString();
        }

        User user = new  User(username, password, name, sex, phone, 1, 1);

        userRepository.save(user);
        String reps  = new Response(Response.Code.Success, user).toString();

        return reps ;
    }


    @PostMapping(value = "/login_by_password")
    @ResponseBody
    public String loginByPassword(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new Response(Response.Code.UserNotExistError).toString();
        }
        if (!password.equals(user.getPassword())) {
            return new Response(Response.Code.PasswordError).toString();
        }
        // 添加cookie
        CookieUtil.addCookie(username, response);
        return new Response(Response.Code.Success).toString();
    }

    @PostMapping(value = "/login_by_face_image")
    @ResponseBody
    public String loginByFaceImage(HttpServletRequest request, HttpServletResponse response) {
        String base64Image = request.getParameter("base64_image");
        if (base64Image == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        base64Image = base64Image.replaceAll(" ","+");
        FaceInfo faceInfo = faceRecognitionService.recognition(base64Image);
        if (faceInfo == null){
            return new Response(Response.Code.ImageError).toString();
        }
        //添加cookie
        CookieUtil.addCookie(faceInfo.getUsername(), response);
        return new Response(Response.Code.Success).toString();
    }

    @RecordLog
    @CheckPermission
    @PostMapping(value = "/set_password")
    @ResponseBody
    public String setPassword(HttpServletRequest request, HttpServletResponse response) {
        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("new_password");
        if (oldPassword == null || newPassword == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        String username = CookieUtil.getUsernameFromRequest(request);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new Response(Response.Code.UserNotExistError).toString();
        }
        if (!oldPassword.equals(user.getPassword())) {
            return new Response(Response.Code.PasswordError).toString();
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return new Response(Response.Code.Success).toString();
    }


    /**
     * 修改人脸标准照片
     */
    @RecordLog
    @CheckPermission
    @PostMapping(value = "/set_face_image")
    @ResponseBody
    public String setFaceImage(HttpServletRequest request, HttpServletResponse response) {
        String base64Image = request.getParameter("base64_image");
        if (base64Image == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        base64Image = base64Image.replaceAll(" ","+");
        String username = CookieUtil.getUsernameFromRequest(request);
        if (!faceRecognitionService.saveFaceInfo(username, base64Image)){
            return new Response(Response.Code.ImageError).toString();
        }
        return new Response(Response.Code.Success).toString();
    }

}
