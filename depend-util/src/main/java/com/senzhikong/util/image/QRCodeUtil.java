package com.senzhikong.util.image;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码生成读取工具
 *
 * @author shu.zhou
 * @version 1.0.0
 * @time 2017年11月10日上午9:25:49
 */
public class QRCodeUtil {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static final int MIN_SIZE = 50;
    private static final int MIN_MARGIN = 0;

    /**
     * 生成二维码图片
     *
     * @param content二维码内容
     * @param size二维码图片大小
     * @param margin二维码图片边距
     * @param logo二维码中心logo图片
     * @param logoSize二维码logo图片大小
     * @return
     */
    public static BufferedImage encodeQrcode(String content, int size, int margin, Image logo, int logoSize) {
        if (content == null || "".equals(content))
            return null;
        if (margin < MIN_MARGIN)
            margin = MIN_MARGIN;
        if (size < MIN_SIZE)
            size = MIN_SIZE;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
        hints.put(EncodeHintType.MARGIN, margin); // 边距
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            if (logo != null) {
                appendQRLogo(image, logo, logoSize);
            }
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码图片
     *
     * @param content 二维码内容
     * @param size 二维码图片大小
     * @param margin 二维码图片边距
     * @return
     */
    public static BufferedImage encodeQrcode(String content, int size, int margin) {
        return encodeQrcode(content, size, margin, null, 0);
    }

    /**
     * @param image 二维码图片
     * @param logo 中心logo图片
     * @param logoSize 中心logo大小
     */
    public static void appendQRLogo(BufferedImage image, Image logo, int logoSize) {
        if (logo == null)
            return;
        Graphics2D gs = image.createGraphics();
        if (logoSize > image.getWidth() * 2 / 5)
            logoSize = image.getWidth() * 2 / 5;
        int logoWidth = logo.getWidth(null) > logoSize ? logoSize : logo.getWidth(null);
        int logoHeight = logo.getHeight(null) > logoSize ? logoSize : logo.getHeight(null);
        int x = (image.getWidth() - logoWidth) / 2;
        int y = (image.getHeight() - logoHeight) / 2;
        gs.drawImage(logo, x, y, logoWidth, logoHeight, null);
        gs.setColor(Color.black);
        gs.setBackground(Color.WHITE);
        gs.dispose();
        logo.flush();
    }


    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    /**
     * 解码
     *
     * @param file
     * @return
     */
    public static String readQRCode(File file) {
        BufferedImage image;
        try {
            if (file == null || file.exists() == false) {
                throw new Exception(" File not found:" + file.getPath());
            }
            image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result;
            // 解码设置编码方式为：utf-8，
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码
     *
     * @param image
     * @return
     */
    public static String readQRCode(BufferedImage image) {
        try {
            if (image == null) {
                throw new Exception(" File is empty:");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result;
            // 解码设置编码方式为：utf-8，
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
