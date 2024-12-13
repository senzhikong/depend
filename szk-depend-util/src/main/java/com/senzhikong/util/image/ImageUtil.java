package com.senzhikong.util.image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * 图片工具类
 *
 * @author zs614
 */
public class ImageUtil {

    /**
     * 读取图标
     *
     * @param file 图标文件路径
     * @return 图标
     */
    public static ImageIcon readImageIcon(String file) {
        return new ImageIcon(readImage(file));
    }

    /**
     * 读取图标
     *
     * @param file 图标文件
     * @return 图标
     */
    public static ImageIcon readImageIcon(File file) {
        return new ImageIcon(readImage(file));
    }

    /**
     * 读取图标
     *
     * @param in 图标文件输入流
     * @return 图标
     */
    public static ImageIcon readImageIcon(InputStream in) {
        return new ImageIcon(readImage(in));
    }

    /**
     * 读取图标
     *
     * @param file   图标文件路径
     * @param width  宽度
     * @param height 高度
     * @return 图标
     */
    public static ImageIcon readImageIcon(String file, int width, int height) {
        return new ImageIcon(readImage(file, width, height));
    }

    /**
     * 读取图标
     *
     * @param file   图标文件
     * @param width  宽度
     * @param height 高度
     * @return 图标
     */
    public static ImageIcon readImageIcon(File file, int width, int height) {
        return new ImageIcon(readImage(file, width, height));
    }

    /**
     * 读取图标
     *
     * @param in     图片输入流
     * @param width  宽度
     * @param height 高度
     * @return 图标
     */
    public static ImageIcon readImageIcon(InputStream in, int width, int height) {
        return new ImageIcon(readImage(in, width, height));
    }

    /**
     * 读取图片
     *
     * @param file 图片路径
     * @return 图片
     */
    public static BufferedImage readImage(String file) {
        return readImage(new File(file));
    }

    /**
     * 读取图片
     *
     * @param file 文件
     * @return 图片
     */
    public static BufferedImage readImage(File file) {
        try (InputStream in = new FileInputStream(file)) {
            return readImage(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取图片
     *
     * @param in 输入流
     * @return 图片
     */
    public static BufferedImage readImage(InputStream in) {
        BufferedImage image;
        try {
            image = ImageIO.read(in);
            in.close();
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取图片
     *
     * @param file   图片文件
     * @param width  宽度
     * @param height 高度
     * @return 图片
     */
    public static BufferedImage readImage(String file, int width, int height) {
        return resize(readImage(file), width, height);
    }

    /**
     * 读取图片
     *
     * @param file   图片文件
     * @param width  宽度
     * @param height 高度
     * @return 图片
     */
    public static BufferedImage readImage(File file, int width, int height) {
        return resize(readImage(file), width, height);
    }

    /**
     * 读取图片
     *
     * @param in     图片输入流
     * @param width  宽度
     * @param height 高度
     * @return 图片
     */
    public static BufferedImage readImage(InputStream in, int width, int height) {
        return resize(readImage(in), width, height);
    }

    /**
     * 图片转输入流
     *
     * @param image 图片
     * @return 输入流
     * @throws Exception 异常
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) throws Exception {
        return bufferedImageToInputStream(image, "png");
    }

    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";

    /**
     * 图片转输入流
     *
     * @param image  图片
     * @param format 图片格式
     * @return 输入流
     * @throws Exception 异常
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image, String format) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (JPG.equalsIgnoreCase(format) || JPEG.equalsIgnoreCase(format)) {
            BufferedImage tag = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics2D g = tag.createGraphics();
            tag = g.getDeviceConfiguration()
                    .createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
            g.fillRect(0, 0, tag.getWidth(null), tag.getHeight(null));
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = tag;
        }
        ImageIO.write(image, format, os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return input;
    }

    /**
     * 压缩图片大小
     *
     * @param image   图片输入流
     * @param targetW 宽度
     * @param targetH 高度
     * @return 压缩后的图片
     */
    public static BufferedImage resize(InputStream image, int targetW, int targetH) {
        return resize(image, targetW, targetH, false);
    }

    /**
     * 压缩图片大小
     *
     * @param image        图片输入流
     * @param targetW      宽度
     * @param targetH      高度
     * @param isProportion 是否等比例缩放
     * @return 压缩后的图片
     */
    public static BufferedImage resize(InputStream image, int targetW, int targetH, boolean isProportion) {
        BufferedImage bi = readImage(image);
        return resize(bi, targetW, targetH, isProportion);
    }

    /**
     * 压缩图片大小
     *
     * @param image   图片输入流
     * @param targetW 宽度
     * @return 压缩后的图片
     */
    public static BufferedImage resize(InputStream image, int targetW) {
        BufferedImage bi = (BufferedImage) readImage(image);
        return resize(bi, targetW);
    }

    /**
     * 压缩图片大小
     *
     * @param source  需要压缩的图片
     * @param targetW 宽度
     * @param targetH 高度
     * @return 压缩后的图片
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        return resize(source, targetW, targetH, false);
    }

    /**
     * 压缩图片大小,等比缩放
     *
     * @param source  需要压缩的图片
     * @param targetW 宽度
     * @return 压缩后的图片
     */
    public static BufferedImage resize(BufferedImage source, int targetW) {
        return resize(source, targetW, source.getHeight(), true);
    }

    /**
     * 压缩图片大小
     *
     * @param source       需要压缩的图片
     * @param targetW      宽度
     * @param targetH      高度
     * @param isProportion 是否等比例缩放
     * @return 压缩后的图片
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean isProportion) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target;
        if (targetW == source.getWidth() && targetH == source.getHeight()) {
            return source;
        }
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // 这里想实现在targetW，targetH范围内实现等比缩放。
        if (isProportion) {
            if (sx < sy) {
                sx = sy;
                targetW = (int) (sx * source.getWidth());
            } else {
                sy = sx;
                targetH = (int) (sy * source.getHeight());
            }
        }
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 图片保存到文件
     *
     * @param image    图片
     * @param filePath 保存路径
     */
    public static void saveImage(BufferedImage image, String filePath) {
        saveImage(image, null, new File(filePath));
    }

    /**
     * 图片保存到文件
     *
     * @param image 图片
     * @param file  保存文件
     */
    public static void saveImage(BufferedImage image, File file) {
        saveImage(image, null, file);
    }

    /**
     * 图片保存到文件
     *
     * @param image 图片
     * @param os    输出流
     */
    public static void saveImage(BufferedImage image, OutputStream os) {
        saveImage(image, null, os);
    }

    /**
     * 图片保存到文件
     *
     * @param image      图片
     * @param formatName 图片格式
     * @param filePath   图片路径
     */
    public static void saveImage(BufferedImage image, String formatName, String filePath) {
        saveImage(image, formatName, new File(filePath));
    }

    /**
     * 图片保存到文件
     *
     * @param image      图片
     * @param formatName 图片格式
     * @param file       图片
     */
    public static void saveImage(BufferedImage image, String formatName, File file) {
        try {
            saveImage(image, formatName, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片保存到文件
     *
     * @param image      图片
     * @param formatName 图片格式
     * @param os         输出流
     */
    public static void saveImage(BufferedImage image, String formatName, OutputStream os) {
        if (formatName == null || formatName.trim().isEmpty()) {
            formatName = "jpg";
        }
        try {
            ImageIO.write(image, formatName, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断文件是否图片
     *
     * @param file 文件
     * @return 是否图片
     */
    public static boolean isImage(File file) {
        try (ImageInputStream is = ImageIO.createImageInputStream(file)) {
            if (null == is) {
                return false;
            }
            is.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件图标
     *
     * @param f 文件
     * @return 文件图片
     */
    public static Icon getFileIcon(File f) {
        if (f != null && f.exists()) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            return fsv.getSystemIcon(f);
        }
        return null;
    }
}
