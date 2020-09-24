package com.whut.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Response {



    // 请求是否成功



    private int code;
    private String msg;
    private Object data;


    public Response(Code code) {
        this.code = code.code;
        this.msg = code.msg;
    }

    public Response(Code code, Object data) {
        this.code = code.code;
        this.msg = code.msg;
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }


    public enum Code {

        ImageStored(1,"图片保存成功"),
        Success(0, "操作成功"),
        ParameterError(-1, "参数错误"),
        UserNotExistError(-2, "用户不存在"),
        PasswordError(-3, "密码错误"),
        UnLoginError(-4, "未登录"),
        SystemError(-5, "未登录"),
        ImageError(-6, "图片中人脸比较不清楚，请更换清晰的照片"),
        UserAndImageNotMatchError(-7, "人脸识别结果与用户不匹配"),

        NoImageFileSelected(-8, "没有选择文件，请选择！");




        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
