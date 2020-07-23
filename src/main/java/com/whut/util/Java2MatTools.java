package com.whut.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import sun.misc.BASE64Decoder;

public class Java2MatTools {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 路径图片转为base64
     *
     * @param imagePath
     * @return
     * @throws IOException
     */
    public static Mat imagePath2Mat(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new FileInputStream(imagePath));
        return Java2MatTools.BufImg2Mat(image, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);
    }

    /**
     * base64转Mat
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static Mat base642Mat(String base64) throws IOException {
        // 对base64进行解码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] origin = decoder.decodeBuffer(base64);
        InputStream in = new ByteArrayInputStream(origin); // 将b作为输入流；
        BufferedImage image = ImageIO.read(in);
        return Java2MatTools.BufImg2Mat(image, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);
    }

    /**
     * @param base64
     * @throws IOException
     */
    public static Rect base642Rect(String base64) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] origin = decoder.decodeBuffer(base64);
        InputStream in = new ByteArrayInputStream(origin); // 将b作为输入流；
        BufferedImage image = ImageIO.read(in);
        return new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * @param
     * @throws IOException
     */
    public static Rect BufferedImage2Rect(BufferedImage image) throws IOException {
        return new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * @param base64
     * @throws IOException
     */
    public static BufferedImage base642BufferedImage(String base64) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] origin = decoder.decodeBuffer(base64);
        InputStream in = new ByteArrayInputStream(origin); // 将b作为输入流；
        return ImageIO.read(in);
    }

    /**
     * BufferedImage转换成Mat
     *
     * @param original 要转换的BufferedImage
     * @param imgType  bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType  转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat BufImg2Mat(BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType) {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

    /**
     * 将bufferdimage转换为图片
     *
     * @param bi
     * @param pathAndName
     * @throws IOException
     */
    public static void writeImageFile(BufferedImage bi, String pathAndName) throws IOException {
        File outputfile = new File(pathAndName);
        ImageIO.write(bi, "jpg", outputfile);
    }

}

