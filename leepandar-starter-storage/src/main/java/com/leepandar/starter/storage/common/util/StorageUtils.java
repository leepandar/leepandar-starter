package com.leepandar.starter.storage.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import software.amazon.awssdk.regions.Region;

import java.time.LocalDate;

/**
 * 存储工具
 */
public class StorageUtils {

    /**
     * 获取区域
     */
    public static Region getRegion(String region) {
        return StrUtil.isEmpty(region) ? Region.US_EAST_1 : Region.of(region);
    }

    /**
     * 生成默认路径：年/月/日/
     */
    public static String generatePath() {
        LocalDate date = LocalDate.now();
        return String.format("%d/%d/%d/", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * 生成文件名：时间戳.扩展名
     */
    public static String generateFileName(String originalFilename) {
        return generateFileName(originalFilename, null, false);
    }

    /**
     * 生成文件名：前缀_时间戳.扩展名
     */
    public static String generateFileName(String originalFilename, String prefix) {
        return generateFileName(originalFilename, prefix, false);
    }

    /**
     * 生成文件名
     *
     * @param originalFilename 原始文件名
     * @param prefix           前缀（可为null）
     * @param useMillis        是否使用毫秒时间戳，false则使用格式化时间戳
     */
    public static String generateFileName(String originalFilename, String prefix, boolean useMillis) {
        String extension = FileUtil.getSuffix(originalFilename);
        String timestamp = useMillis
            ? String.valueOf(System.currentTimeMillis())
            : DateUtil.format(DateUtil.date(), "yyyyMMddHHmmssSSS");

        String fileName = StrUtil.isNotBlank(prefix) ? prefix + "_" + timestamp : timestamp;
        return fileName + (StrUtil.isNotBlank(extension) ? "." + extension : "");
    }
}
