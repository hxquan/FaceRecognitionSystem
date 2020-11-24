package com.whut.controller;

import com.whut.dao.PunchRecordRepository;
import com.whut.dao.entity.PunchRecord;
import com.whut.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PunchRecordController {
    @Autowired
    PunchRecordRepository punchRecordRepository;

    @GetMapping("records")
    public String getAllPunchRecord(Model model){

        List<PunchRecord> punchRecords = punchRecordRepository.findAll();

        //放在请求域中
        model.addAttribute("records",punchRecords);
        // thymeleaf默认就会拼串
        // classpath:/templates/xxxx.html
        System.out.println("收到records请求");
        return "record/list";

    }

}
