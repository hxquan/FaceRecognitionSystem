package com.whut.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.aop.interfaces.CheckPermission;
import com.whut.aop.interfaces.RecordLog;
import com.whut.dao.PunchRecordRepository;
import com.whut.dao.UserRepository;
import com.whut.dao.entity.FaceInfo;
import com.whut.dao.entity.PunchRecord;
import com.whut.dao.entity.User;
import com.whut.domain.Response;
import com.whut.service.FaceRecognitionService;
import com.whut.util.CookieUtil;
import com.whut.util.PunchRecordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

/**
 * 打卡接口
 */
@Controller
@RequestMapping("/punch")
public class PunchController {

    @Autowired
    private PunchRecordRepository punchRecordRepository;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Autowired
    private UserRepository userRepository;


    private static class Data{
        User user;
        PunchRecord punchRecord;

        public Data(User user, PunchRecord punchRecord) {
            this.user = user;
            this.punchRecord = punchRecord ;
        }

        @Override
        public String toString() {
            return  "{" +
                    "\"user\":" + user.toJSON() +
                    ", \"punchRecord\":" + punchRecord.toJSON() +
                    "}";
        }
    }


    /**
     * 打卡
     */
    @RecordLog
//    @CheckPermission
    @PostMapping(value = "/punch")
    @ResponseBody
    public String punch(HttpServletRequest request, HttpServletResponse response) {
        String base64Image = request.getParameter("base64_image");
        if (base64Image == null) {
            return new Response(Response.Code.ParameterError).toString();
        }
        base64Image = base64Image.replaceAll(" ","+");
        String username = CookieUtil.getUsernameFromRequest(request);

        FaceInfo faceInfo = faceRecognitionService.recognition(base64Image);
        if (faceInfo == null){
            return new Response(Response.Code.ImageError).toString();
        }
        // 问题所在
        if (!faceInfo.getUsername().equals(username)) {
            return new Response(Response.Code.UserAndImageNotMatchError).toString();
        }
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        Time time = new Time(timestamp);
        String sequenceNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0,7) + timestamp;
        PunchRecord currentPunchRecord = new PunchRecord(sequenceNo, username, date, time);
        punchRecordRepository.save(currentPunchRecord);
        User user = userRepository.findByUsername(username);
//        String resp = new Response(Response.Code.Success, new Data(user, faceInfo)).toString();
//
        List<PunchRecord> punchRecordList = punchRecordRepository.findAllByUsername(username);
//        PunchRecord currentPunchRecord = PunchRecordUtil.findCurrentRecord(punchRecordList) ;
        Data  data = new Data(user, currentPunchRecord) ;

        String resp = new Response(Response.Code.Success, data.toString()).toString();
        System.out.println(resp);
        return resp ;
    }








    /**
     * 打卡
     */
    @RecordLog
    @PostMapping(value = "/simplePunch")
    @ResponseBody
//    public String simplePunch(HttpServletRequest request, HttpServletResponse response) {
    public String simplePunch(@RequestParam("image") MultipartFile file, HttpServletRequest request) {
//        String base64Image = request.getParameter("base64_image");
        long t1 = System.currentTimeMillis();
        String base64Image = null ;
        if (!file.isEmpty()) {
            try {
                BASE64Encoder encoder = new BASE64Encoder();
                // 通过base64来转化图片
                base64Image = encoder.encode(file.getBytes());
//                System.out.println(base64Image);
                System.out.println("base64收到");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String result;

        if (base64Image == null) {
//            return new Response(Response.Code.ParameterError).toString();
            result = "图片上传失败";
//            System.out.println("result: "  + result);


            return result;
        }
        base64Image = base64Image.replaceAll(" ","+");

        FaceInfo faceInfo = faceRecognitionService.recognition(base64Image);
        if (faceInfo == null){
//            return new Response(Response.Code.ImageError).toString();
            result = "未注册或照片不清晰";
//            System.out.println("result: "  + result);
            return result;
        }

        String  username  =  faceInfo.getUsername();

        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        Time time = new Time(timestamp);
        String sequenceNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0,7) + timestamp;
        PunchRecord currentPunchRecord = new PunchRecord(sequenceNo, username, date, time);
        punchRecordRepository.save(currentPunchRecord);
        User user = userRepository.findByUsername(username);
        Data  data = new Data(user, currentPunchRecord) ;

        String resp = new Response(Response.Code.Success, data.toString()).toString();
        System.out.println(resp);
        result = username + " 打卡成功" + " " + date + " " + time;

//        System.out.println("result: "  + result);
        long t2 = System.currentTimeMillis();
        double recongnitionTime = ( t2- t1 ) / 1000.0;
        System.out.println("运行时间：" + recongnitionTime);

        return result ;
    }





    @RecordLog
    @PostMapping(value = "/punchId")
    @ResponseBody
    public String punchId(HttpServletRequest request){


        String username = request.getParameter("username");
        String id = request.getParameter("id");
        String password = request.getParameter("password");

        User user = userRepository.findById(Integer.valueOf(id));

        if (user.getName().equals(username)  &&user.getPassword().equals(password) ){
            return "打卡成功";
        }

        return  "用户不存在或者密码错误！";


    }





    /**
     * 打卡
     */
    @RecordLog
//    @CheckPermission
    @PostMapping(value = "/get_punch_record")
    @ResponseBody
    public String getPunchRecord(HttpServletRequest request, HttpServletResponse response) {
        String fromDate = request.getParameter("from_date");
        String toDate = request.getParameter("to_date");

        String username = CookieUtil.getUsernameFromRequest(request);
        List<PunchRecord> punchRecords =  punchRecordRepository.findAllByUsername(username);

        JSONArray array = new JSONArray();
        for (PunchRecord record: punchRecords){
            array.add(record.toJSON());
        }
        System.out.println(array);
        return new Response(Response.Code.Success, array).toString();
    }


}
