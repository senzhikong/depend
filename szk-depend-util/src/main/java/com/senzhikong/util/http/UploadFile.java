package com.senzhikong.util.http;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.InputStream;

/**
 * @author shu
 */
@Getter
@Setter
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
}
