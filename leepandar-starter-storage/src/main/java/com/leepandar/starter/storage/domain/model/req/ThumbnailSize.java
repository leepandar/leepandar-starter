package com.leepandar.starter.storage.domain.model.req;

/**
 * 缩略图尺寸
 */
public class ThumbnailSize {
    private int width;
    private int height;
    private boolean keepAspectRatio = true;

    public ThumbnailSize(int width, int height) {
        this.width = width;
        this.height = height;
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

    public boolean isKeepAspectRatio() {
        return keepAspectRatio;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }
}