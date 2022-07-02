package com.senzhikong.util.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author shu
 */
public class ResponseEntity {
    private InputStream inputStream;
    private byte[] bytes = null;
    private String resultString;

    public ResponseEntity(InputStream in) {
        try {
            bytes = new byte[in.available()];
            in.read(bytes);
        } catch (Exception e) {
        }
    }

    public ResponseEntity(byte[] bytes) {
        this.bytes = bytes;
    }

    public InputStream getInputStream() {
        if (inputStream == null && bytes != null) {
            inputStream = new ByteArrayInputStream(bytes);
        }
        return inputStream;
    }

    protected void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getResultString(String encoding) {
        try {
            resultString = new String(bytes, encoding);
        } catch (Exception e) {
        }
        return resultString;
    }

    public String getResultString() {
        resultString = new String(bytes);
        return resultString;
    }
}
