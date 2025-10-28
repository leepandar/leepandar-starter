package com.leepandar.starter.storage.common.constant;

import com.leepandar.starter.core.constant.StringConstants;

/**
 * 存储常数
 */
public class StorageConstant {

    /**
     * 默认存储平台
     */
    public static final String DEFAULT_STORAGE_PLATFORM = "local";

    /**
     * 配置文件
     */
    public static final String CONFIG = "CONFIG";

    /**
     * 动态配置
     */
    public static final String DYNAMIC = "DYNAMIC";

    /**
     * 默认文件大小
     */
    public static final Long DEFAULT_FILE_SIZE = 1024 * 1024 * 10L;

    /**
     * 默认的对象ACL
     */
    public static final String DEFAULT_ACL = "private";

    /**
     * 缩略图后缀
     */
    public static final String THUMBNAIL_SUFFIX = StringConstants.DOT + "thumb" + StringConstants.DOT;

    /**
     * ContentType 图片前缀
     */
    public static final String CONTENT_TYPE_IMAGE = "image/";

}
