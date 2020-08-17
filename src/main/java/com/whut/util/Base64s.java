package com.whut.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.IO;
import org.omg.CORBA.portable.OutputStream;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64s {

    public static String encode(String text)
    {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(textByte);
    }

    public static String decode(String encodedText)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
    }



    public static String encodeToStr(String imagePath) throws IOException {
        String type = StringUtils.substring(imagePath,imagePath.lastIndexOf(".") + 1 );
        BufferedImage image = ImageIO.read(new File(imagePath));
        String  imageStr = null ;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            // 将图片写入输出流中。
            byte [] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();

            imageStr = encoder.encode(imageBytes);
            bos.close();


        } catch (IOException io){
            io.printStackTrace();
        }

        return imageStr ;


    }


    public static void main(String[] args)  {
        try {

//        String str = encodeToStr("C:\\Users\\daronlee\\Pictures\\Saved Pictures\\728080730.bmp");
        String str = encodeToStr("C:\\Users\\daronlee\\Documents\\Tencent Files\\" +
                "1071712435\\FileRecv\\富山-人脸识别素材图片20200728\\富山-人脸识别素材图片20200728\\728081125.bmp");
        System.out.println(str);

        File  strFile = new  File("C:\\Users\\daronlee\\Pictures\\Saved Pictures\\728081125");

        FileOutputStream fileOutputStream = new FileOutputStream(strFile);
        fileOutputStream.write(str.getBytes());
        fileOutputStream.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }


}
