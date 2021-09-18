package com.senzhikong.web.util;

import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author Shu.zhou
 */
public class VerifyCodeUtils {

    public static final String VERIFY_CODES = "23456789abcdefgknpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private static final Random random = new Random();

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param verifySize 验证码长度
     */
    public static String generateVerifyCode(int verifySize) {
        return generateVerifyCode(verifySize, VERIFY_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param verifySize 验证码长度
     * @param sources    验证码字符源
     */
    public static String generateVerifyCode(int verifySize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 输出指定验证码图片流
     */
    public static BufferedImage outputImage(int w, int h, String code) {
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(Color.GRAY);// 设置边框色
//        g2.fillRect(0, 0, w, h);

        Color c = getRandColor(200, 230);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 0, w, h);

        // 添加噪点
//        float yawpRate = 0.05f;// 噪声率
//        int area = (int) (yawpRate * w * h);
//        for (int i = 0; i < area; i++) {
//            int x = random.nextInt(w);
//            int y = random.nextInt(h);
//            int rgb = getRandomIntColor();
//            image.setRGB(x, y, rgb);
//        }
        g2.setColor(getRandColor(50, 100));
        float fontSize = h - h / 8f;
        Font font = g2.getFont().deriveFont(fontSize);
        g2.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            AffineTransform affine = new AffineTransform();
            int x = (int) (w / verifySize * i + fontSize / 2);
            int y = h / 2;
            affine.setToRotation(Math.PI / 10 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), x, y);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, (int) (h / 2 + fontSize / 2 - 5));
        }

        g2.dispose();
        return image;
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    // 保存图片到指定的输出流
    public static String createCode(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        return createCode(response.getOutputStream(), "jpg", 4);
    }

    // 保存图片到指定的输出流
    public static String createCode(OutputStream outputStream) throws IOException {
        return createCode(outputStream, "jpg", 4);
    }

    public static String createCode(OutputStream outputStream, String type, int length) throws IOException {
        String verifyCode = generateVerifyCode(length);
        BufferedImage image = outputImage(150, 50, verifyCode);
        ImageIO.write(image, type, outputStream);
        return verifyCode;
    }

    public static void writeCodeImage(OutputStream outputStream, String verifyCode) throws IOException {
        BufferedImage image = outputImage(150, 50, verifyCode);
        ImageIO.write(image, "jpg", outputStream);
    }
}
