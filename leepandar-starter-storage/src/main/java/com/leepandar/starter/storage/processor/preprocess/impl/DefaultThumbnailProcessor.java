package com.leepandar.starter.storage.processor.preprocess.impl;

import cn.hutool.core.io.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import com.leepandar.starter.storage.common.constant.StorageConstant;
import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.model.context.UploadContext;
import com.leepandar.starter.storage.domain.model.req.ThumbnailInfo;
import com.leepandar.starter.storage.domain.model.req.ThumbnailSize;
import com.leepandar.starter.storage.processor.preprocess.ThumbnailProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 默认缩略图处理器
 */
public class DefaultThumbnailProcessor implements ThumbnailProcessor {

    @Override
    public String getName() {
        return DefaultThumbnailProcessor.class.getSimpleName();
    }

    @Override
    public boolean support(UploadContext context) {
        String contentType = context.getFile().getContentType();
        return contentType != null && contentType.startsWith(StorageConstant.CONTENT_TYPE_IMAGE);
    }

    @Override
    public ThumbnailInfo process(UploadContext context, InputStream sourceInputStream) {
        try {
            String suffix = FileUtil.getSuffix(context.getFormatFileName());
            ThumbnailSize size = context.getThumbnailSize();
            BufferedImage thumbnail = Thumbnails.of(sourceInputStream)
                .size(size.getWidth(), size.getHeight())
                .keepAspectRatio(size.isKeepAspectRatio())
                .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, suffix, baos);

            ThumbnailInfo info = new ThumbnailInfo();
            info.setData(baos.toByteArray());
            info.setFormat(suffix);
            info.setWidth(thumbnail.getWidth());
            info.setHeight(thumbnail.getHeight());

            return info;
        } catch (Exception e) {
            throw new StorageException("生成缩略图失败", e);
        }
    }
}