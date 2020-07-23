package com.whut.dao.entity;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "punch_record")
public class PunchRecord {

    @Id
    @GeneratedValue
    private int id;
    //流水号
    @Column(name = "sequence_no")
    private String sequenceNo;
    //工号
    @Column(name = "username")
    private String username;
    //日期
    @Column(name = "date")
    private Date date;
    //时间
    @Column(name = "time")
    private Time time;


    public PunchRecord() {
    }

    public PunchRecord(String sequenceNo, String username, Date date, Time time) {
        this.sequenceNo = sequenceNo;
        this.username = username;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public JSONObject toJSON(){
        JSONObject obj= new JSONObject();
        obj.put("sequence_no", sequenceNo);
        obj.put("date", date.toLocalDate());
        obj.put("time", time.toLocalTime());
        obj.put("username", username);
        return obj;
    }
}
