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
     * @param file
     * @return
     */
    public static ImageIcon readImageIcon(String file) {
        ImageIcon icon = new ImageIcon(readImage(file));
        return icon;
    }

    /**
     * 读取图标
     *
     * @param file
     * @return
     */
    public static ImageIcon readImageIcon(File file) {
        ImageIcon icon = new ImageIcon(readImage(file));
        return icon;
    }

    /**
     * 读取图标
     *
     * @param in
     * @return
     */
    public static ImageIcon readImageIcon(InputStream in) {
        ImageIcon icon = new ImageIcon(readImage(in));
        return icon;
    }

    /**
     * 读取图标
     *
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon readImageIcon(String file, int width, int height) {
        ImageIcon icon = new ImageIcon(readImage(file, width, height));
        return icon;
    }

    /**
     * 读取图标
     *
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon readImageIcon(File file, int width, int height) {
        ImageIcon icon = new ImageIcon(readImage(file, width, height));
        return icon;
    }

    /**
     * 读取图标
     *
     * @param in
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon readImageIcon(InputStream in, int width, int height) {
        ImageIcon icon = new ImageIcon(readImage(in, width, height));
        return icon;
    }

    /**
     * 读取图片
     *
     * @param file
     * @return
     */
    public static BufferedImage readImage(String file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readImage(in);
    }

    /**
     * 读取图片
     *
     * @param file
     * @return
     */
    public static BufferedImage readImage(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return readImage(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取图片
     *
     * @param in
     * @return
     */
    public static BufferedImage readImage(InputStream in) {
        BufferedImage image = null;
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
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage readImage(String file, int width, int height) {
        return resize((BufferedImage) readImage(file), width, height);
    }

    /**
     * 读取图片
     *
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage readImage(File file, int width, int height) {
        return resize((BufferedImage) readImage(file), width, height);
    }

    /**
     * 读取图片
     *
     * @param in
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage readImage(InputStream in, int width, int height) {
        return resize((BufferedImage) readImage(in), width, height);
    }

    /**
     * 图片转输入流
     *
     * @param image
     * @return
     * @throws Exception
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) throws Exception {
        return bufferedImageToInputStream(image, "png");
    }

    /**
     * 图片转输入流
     *
     * @param image
     * @param format
     * @return
     * @throws Exception
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image, String format) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if ("jpg".equalsIgnoreCase(format) || "jpeg".equalsIgnoreCase(format)) {
            BufferedImage tag = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics2D g = tag.createGraphics();
            tag = g.getDeviceConfiguration()
                    .createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
            g.fillRect(0, 0, tag.getWidth(null), tag.getHeight(null));
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
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
     * @param image
     * @param targetW
     * @param targetH
     * @return
     */
    public static BufferedImage resize(InputStream image, int targetW, int targetH) {
        return resize(image, targetW, targetH, false);
    }

    /**
     * 压缩图片大小
     *
     * @param image
     * @param targetW
     * @param targetH
     * @param isProportion
     * @return
     */
    public static BufferedImage resize(InputStream image, int targetW, int targetH, boolean isProportion) {
        BufferedImage bi = (BufferedImage) readImage(image);
        return resize(bi, targetW, targetH, isProportion);
    }

    /**
     * 压缩图片大小
     *
     * @param image
     * @param targetW
     * @return
     */
    public static BufferedImage resize(InputStream image, int targetW) {
        BufferedImage bi = (BufferedImage) readImage(image);
        return resize(bi, targetW);
    }

    /**
     * 压缩图片大小
     *
     * @param source
     * @param targetW
     * @param targetH
     * @return
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        return resize(source, targetW, targetH, false);
    }

    /**
     * 压缩图片大小,等比缩放
     *
     * @param source
     * @param targetW
     * @return
     */
    public static BufferedImage resize(BufferedImage source, int targetW) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        if (targetW == source.getWidth()) {
            return source;
        }
        int targetH = targetW * source.getHeight() / source.getWidth();
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
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
     * 压缩图片大小
     *
     * @param source
     * @param targetW
     * @param targetH
     * @return
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean isProportion) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
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

    public static void saveImage(BufferedImage image, String filePath) throws Exception {
        saveImage(image, null, new FileOutputStream(filePath));
    }

    public static void saveImage(BufferedImage image, File file) throws Exception {
        saveImage(image, null, new FileOutputStream(file));
    }

    public static void saveImage(BufferedImage image, OutputStream os) throws Exception {
        saveImage(image, null, os);
    }

    public static void saveImage(BufferedImage image, String formatName, String filePath) throws Exception {
        saveImage(image, formatName, new FileOutputStream(filePath));
    }

    public static void saveImage(BufferedImage image, String formatName, File file) throws Exception {
        saveImage(image, formatName, new FileOutputStream(file));
    }

    public static void saveImage(BufferedImage image, String formatName, OutputStream os) throws Exception {
        if (formatName == null || "".equals(formatName.trim())) {
            formatName = "jpg";
        }
        ImageIO.write(image, formatName, os);
    }

    public static boolean isImage(File file) {
        boolean flag = false;
        try {
            ImageInputStream is = ImageIO.createImageInputStream(file);
            if (null == is) {
                return flag;
            }
            is.close();
            flag = true;
        } catch (Exception e) {
        }
        return flag;
    }

    /**
     * 获取大图标
     * @param f
     * @return
     */
    /*
     * public static ImageIcon getBigIcon(File f) { if (f != null && f.exists()) { try { ShellFolder sf = ShellFolder.getShellFolder(f); return (new ImageIcon(sf.getIcon(true))); } catch (Exception e) { e.printStackTrace(); } } return (null); }
     */

    /**
     * 获取小图标
     *
     * @param f
     * @return
     */
    public static Icon getSmallIcon(File f) {
        if (f != null && f.exists()) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            return fsv.getSystemIcon(f);
        }
        return null;
    }
}
