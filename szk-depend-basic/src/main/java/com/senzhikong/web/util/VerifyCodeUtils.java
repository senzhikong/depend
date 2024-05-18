package com.senzhikong.web.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
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
    private static final Random RANDOM = new Random();

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

        Color c = getRandColor(200, 230);
        // 设置背景色
        g2.setColor(c);
        g2.fillRect(0, 0, w, h);

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
        int max = 255;
        if (fc > max) {
            fc = max;
        }
        if (bc > max) {
            bc = max;
        }
        int r = fc + RANDOM.nextInt(bc - fc);
        int g = fc + RANDOM.nextInt(bc - fc);
        int b = fc + RANDOM.nextInt(bc - fc);
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
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = RANDOM.nextInt(255);
        }
        return rgb;
    }

    /**
     * 创建验证码并保存图片到指定的输出流
     *
     * @param response http答应
     * @return 验证码
     * @throws IOException 流未找到异常
     */
    public static String createCode(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        return createCode(response.getOutputStream(), "jpg", 4);
    }

    /**
     * 创建验证码并保存图片到指定的输出流
     *
     * @param outputStream 输出流
     * @return 验证码
     * @throws IOException 流未找到异常
     */
    public static String createCode(OutputStream outputStream) throws IOException {
        return createCode(outputStream, "jpg", 4);
    }

    /**
     * 创建验证码并保存图片到指定的输出流
     *
     * @param outputStream 输出流
     * @param type         图片类型
     * @param length       长度
     * @return 验证码
     * @throws IOException 流未找到异常
     */
    public static String createCode(OutputStream outputStream, String type, int length) throws IOException {
        String verifyCode = generateVerifyCode(length);
        BufferedImage image = outputImage(150, 50, verifyCode);
        ImageIO.write(image, type, outputStream);
        return verifyCode;
    }

    /**
     * 验证码写入到文件流
     *
     * @param outputStream 输出流
     * @param verifyCode   验证码
     * @throws IOException 流未找到异常
     */
    public static void writeCodeImage(OutputStream outputStream, String verifyCode) throws IOException {
        BufferedImage image = outputImage(150, 50, verifyCode);
        ImageIO.write(image, "jpg", outputStream);
    }
}
