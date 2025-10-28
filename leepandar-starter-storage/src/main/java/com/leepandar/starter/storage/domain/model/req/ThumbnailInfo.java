package com.leepandar.starter.storage.domain.model.req;

/**
 * 缩略图信息
 */
public class ThumbnailInfo {
    private byte[] data;
    private String format;
    private int width;
    private int height;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}