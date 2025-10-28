package com.leepandar.starter.storage.processor.progress;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 进度监听输入流包装器
 */
public class ProgressInputStream extends FilterInputStream {

    private final ProgressTracker tracker;

    public ProgressInputStream(InputStream in, ProgressTracker tracker) {
        super(in);
        this.tracker = tracker;
        tracker.start();
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b != -1) {
            tracker.updateProgress(1);
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead > 0) {
            tracker.updateProgress(bytesRead);
        }
        return bytesRead;
    }

    @Override
    public void close() throws IOException {
        super.close();
        tracker.complete();
    }
}
