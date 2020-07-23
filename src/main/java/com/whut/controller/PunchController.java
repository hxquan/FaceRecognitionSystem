package com.whut.controller;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
        FaceInfo faceInfo;

        public Data(User user, FaceInfo faceInfo) {
            this.user = user;
            this.faceInfo = faceInfo;
        }
    }

    /**
     * 打卡
     */
    @RecordLog
    @CheckPermission
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
        if (!faceInfo.getUsername().equals(username)) {
            return new Response(Response.Code.UserAndImageNotMatchError).toString();
        }
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        Time time = new Time(timestamp);
        String sequenceNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0,7) + timestamp;
        punchRecordRepository.save(new PunchRecord(sequenceNo, username, date, time));
        User user = userRepository.findByUsername(username);
        return new Response(Response.Code.Success, new Data(user, faceInfo)).toString();
    }


    /**
     * 打卡
     */
    @RecordLog
    @CheckPermission
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
