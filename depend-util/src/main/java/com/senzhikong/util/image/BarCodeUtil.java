package com.senzhikong.util.image;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 条形码生成读取工具
 *
 * @author shu.zhou
 * @version 1.0.0
 * @time 2017年11月10日上午9:27:16
 */
public class BarCodeUtil {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static final int MIN_WIDTH = 50;
    private static final int MIN_HEIGHT = 20;
    private static final int MIN_MARGIN = 0;

    /**
     * 生成二维码图片
     *
     * @param content条形码内容
     * @param width条形码宽
     * @param height条形码高
     * @param margin条形码边距
     * @return
     */
    public static BufferedImage encodeQrcode(String content, int width, int height, int margin) {
        if (content == null || "".equals(content))
            return null;
        if (margin < MIN_MARGIN)
            margin = MIN_MARGIN;
        if (width < MIN_WIDTH)
            width = MIN_WIDTH;
        if (height < MIN_HEIGHT)
            height = MIN_HEIGHT;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
        hints.put(EncodeHintType.MARGIN, margin); // 边距
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.CODE_128, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public static String readBarCode(File file) {
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
    public static String readBarCode(BufferedImage image) {
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
