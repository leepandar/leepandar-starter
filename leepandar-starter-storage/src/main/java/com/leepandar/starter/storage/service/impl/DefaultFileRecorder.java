package com.leepandar.starter.storage.service.impl;

import com.leepandar.starter.storage.domain.model.resp.FileInfo;
import com.leepandar.starter.storage.domain.model.resp.FilePartInfo;
import com.leepandar.starter.storage.service.FileRecorder;

import java.util.List;

/**
 * 默认文件记录器
 */
public class DefaultFileRecorder implements FileRecorder {
    @Override
    public boolean save(FileInfo fileInfo) {
        return false;
    }

    @Override
    public boolean update(FileInfo fileInfo) {
        return false;
    }

    @Override
    public boolean delete(String platform, String path) {
        return false;
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {

    }

    @Override
    public List<FilePartInfo> getFileParts(String fileId) {
        return List.of();
    }

    @Override
    public void deleteFileParts(String fileId) {

    }
}
