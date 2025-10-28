package com.leepandar.starter.storage.domain.file;

import org.springframework.web.multipart.MultipartFile;
import com.leepandar.starter.storage.processor.progress.ProgressInputStream;
import com.leepandar.starter.storage.processor.progress.ProgressTracker;
import com.leepandar.starter.storage.processor.progress.UploadProgressListener;

import java.io.*;

/**
 * 进度监听 MultipartFile 包装器
 */
public class ProgressAwareMultipartFile extends EnhancedMultipartFile {

    private final UploadProgressListener progressListener;
    private volatile ProgressTracker progressTracker;
    private volatile boolean progressEnabled = false;
    private final Object progressLock = new Object();

    // 用于区分不同的读取阶段
    public enum ReadPhase {
        VALIDATION,     // 验证阶段
        THUMBNAIL,      // 缩略图生成
        UPLOAD          // 实际上传
    }

    private volatile ReadPhase currentPhase = ReadPhase.VALIDATION;

    public ProgressAwareMultipartFile(MultipartFile originalFile,
                                      boolean enableCache,
                                      UploadProgressListener progressListener) {
        super(originalFile, enableCache);
        this.progressListener = progressListener;
    }

    /**
     * 设置当前读取阶段
     */
    public void setReadPhase(ReadPhase phase) {
        synchronized (progressLock) {
            this.currentPhase = phase;
            // 只在上传阶段启用进度
            this.progressEnabled = (phase == ReadPhase.UPLOAD);

            // 切换到上传阶段时，重置进度追踪器
            if (phase == ReadPhase.UPLOAD && progressListener != null) {
                this.progressTracker = new ProgressTracker(getSize(), progressListener);
            }
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream originalStream = super.getInputStream();

        synchronized (progressLock) {
            // 只有在上传阶段且有监听器时才包装流
            if (progressEnabled && progressTracker != null) {
                return new ProgressInputStream(originalStream, progressTracker);
            }
        }

        return originalStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        if (progressEnabled && progressTracker != null) {
            // 使用带进度的传输
            try (InputStream in = getInputStream(); OutputStream out = new FileOutputStream(dest)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            super.transferTo(dest);
        }
    }

    /**
     * 获取不带进度监听的输入流（向后兼容）
     */
    public InputStream getInputStreamWithoutProgress() throws IOException {
        return super.getInputStream();
    }

    /**
     * 清理资源
     */
    @Override
    public void clearCache() {
        super.clearCache();
        synchronized (progressLock) {
            progressTracker = null;
        }
    }
}
