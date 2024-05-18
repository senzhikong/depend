package com.senzhikong.util.http;

/**
 * @author shu
 */
public class DownloadFile {

    private String contentType;
    private String fileName;
    private long totalSize;
    private long now;
    private long remnant;
    private double rate;
    private boolean finish;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getRemnant() {
        return remnant;
    }

    public void setRemnant(long remnant) {
        this.remnant = remnant;
    }

    public double getRate() {
        rate = now / totalSize;
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }


    public String getContentType() {
        return contentType;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
