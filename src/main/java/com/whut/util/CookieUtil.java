package com.whut.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String COOKIE_NAME = "StatusMonitor";

    /**
     * cookie生成算法
     * @param openID 用户的openID
     */
    private static String genCookieValue(String openID)
    {
        return Base64s.encode(openID);
    }

    /**
     * 设置浏览器cookie，方便以后可以无密码登陆
     */
    public static void addCookie(String username, HttpServletResponse response)
    {
        String value = genCookieValue(username);
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setMaxAge(3600*24*30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 用户注销后，要清除cookie
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(COOKIE_NAME)){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 从cookie中解析出用户名和加密的密码
     */
    public static String getUsernameFromRequest(HttpServletRequest request)
    {
        Cookie[] cookies =  request.getCookies();
        String encodedText = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(COOKIE_NAME)){
                    encodedText = cookie.getValue();
                    break;
                }
            }
        }
        if (encodedText == null)
        {
            return null;
        }
        return Base64s.decode(encodedText);
    }
}
