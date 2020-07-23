package com.whut.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "face_info")
public class FaceInfo {
    @Id
    @GeneratedValue
    private int id;
    //根据图片计算得到的编码信息
    @Column(name = "face_encodings")
    private String faceEncodings;
    //用户标准照片的base64编码
    @Column(name = "base64_image")
    private String base64Image;
    //外键
    @Column(name = "username")
    private String username;

    public FaceInfo(String faceEncodings, String base64Image, String username) {
        this.faceEncodings = faceEncodings;
        this.base64Image = base64Image;
        this.username = username;
    }


    public FaceInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFaceEncodings() {
        return faceEncodings;
    }

    public void setFaceEncodings(String faceEncodings) {
        this.faceEncodings = faceEncodings;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}