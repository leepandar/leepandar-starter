package com.leepandar.starter.cache.redisson.util;

import com.leepandar.starter.core.util.SpringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁工具类
 */
public class RedisLockUtils implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(RedisLockUtils.class);

    /**
     * 默认锁过期时间（毫秒）
     */
    private static final long DEFAULT_EXPIRE_TIME = 10000L;

    /**
     * 默认获取锁超时时间（毫秒）
     */
    private static final long DEFAULT_TIMEOUT = 5000L;

    /**
     * Redisson 客户端
     */
    private static volatile RedissonClient CLIENT;

    /**
     * 锁实例
     */
    private final RLock lock;

    /**
     * 是否成功获取锁
     */
    private boolean isLocked;

    /**
     * 获取Redisson客户端实例
     *
     * @return RedissonClient实例
     */
    private static RedissonClient getClient() {
        if (CLIENT == null) {
            synchronized (RedisLockUtils.class) {
                if (CLIENT == null) {
                    CLIENT = SpringUtils.getBean(RedissonClient.class, false);
                }
            }
        }
        return CLIENT;
    }

    /**
     * 私有构造函数，防止外部实例化
     */
    private RedisLockUtils(RLock lock, long expireTime, long timeout, TimeUnit unit) {
        this.lock = lock;
        try {
            this.isLocked = lock.tryLock(timeout, expireTime, unit);
            if (isLocked) {
                log.debug("获取锁成功，key: {}", lock.getName());
            } else {
                log.debug("获取锁失败，key: {}", lock.getName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取锁过程中被中断，key: {}", lock.getName(), e);
        }
    }

    /**
     * 尝试获取锁（启用看门狗自动续期机制）
     *
     * @param key     锁的键
     * @param timeout 获取锁的超时时间
     * @param unit    时间单位
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLockWithWatchdog(String key, long timeout, TimeUnit unit) {
        RLock lock = getClient().getLock(key);
        // 传入-1表示使用看门狗机制
        return new RedisLockUtils(lock, -1, timeout, unit);
    }

    /**
     * 尝试获取锁（启用看门狗自动续期机制，默认时间单位为毫秒）
     *
     * @param key     锁的键
     * @param timeout 获取锁的超时时间（单位：毫秒）
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLockWithWatchdog(String key, long timeout) {
        return tryLockWithWatchdog(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 尝试获取锁（启用看门狗自动续期机制，使用默认超时时间）
     *
     * @param key 锁的键
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLockWithWatchdog(String key) {
        return tryLockWithWatchdog(key, DEFAULT_TIMEOUT);
    }

    /**
     * 尝试获取锁
     *
     * @param key        锁的键
     * @param expireTime 锁的过期时间
     * @param timeout    获取锁的超时时间
     * @param unit       时间单位
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLock(String key, long expireTime, long timeout, TimeUnit unit) {
        RLock lock = getClient().getLock(key);
        return new RedisLockUtils(lock, expireTime, timeout, unit);
    }

    /**
     * 尝试获取锁（默认时间单位为毫秒）
     *
     * @param key        锁的键
     * @param expireTime 锁的过期时间（单位：毫秒）
     * @param timeout    获取锁的超时时间（单位：毫秒）
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLock(String key, long expireTime, long timeout) {
        return tryLock(key, expireTime, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 尝试获取锁（使用默认过期时间和超时时间）
     *
     * @param key 锁的键
     * @return LockUtils 实例
     */
    public static RedisLockUtils tryLock(String key) {
        return tryLock(key, DEFAULT_EXPIRE_TIME, DEFAULT_TIMEOUT);
    }

    /**
     * 检查是否成功获取锁
     *
     * @return true：成功；false：失败
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * 释放锁
     */
    @Override
    public void close() {
        if (isLocked && lock.isHeldByCurrentThread()) {
            try {
                lock.unlockAsync().get();
                log.debug("释放锁成功，key: {}", lock.getName());
            } catch (Exception e) {
                log.error("释放锁失败，key: {}", lock.getName(), e);
            }
        } else {
            log.debug("锁未被当前线程持有，无需释放，key: {}", lock.getName());
        }
    }
}
