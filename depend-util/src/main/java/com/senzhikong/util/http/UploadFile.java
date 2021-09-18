package com.senzhikong.util.http;

import java.io.File;
import java.io.InputStream;

public class UploadFile {

    private String fieldName;
    private String fileName;
    private File file;
    private byte[] fileBuffer;
    private InputStream fileStream;

    public UploadFile() {

    }

    public UploadFile(String fieldName, String fileName, File file) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.file = file;
    }

    public UploadFile(String fieldName, String fileName, InputStream fileStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileStream = fileStream;
    }

    public UploadFile(String fieldName, String fileName, byte[] fileBuffer) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileBuffer = fileBuffer;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }
}
