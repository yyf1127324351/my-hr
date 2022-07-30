package com.myhr.hr.service.redis;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 缓存工具类
 *
 */
public interface RedisService {

	/**
	 * 简单的key-value 存储
	 */
	void setKey_Value(String key, String value, int expiration);

	/**
	 * 简单的Key-value获取值
	 */
	String getValueByKey(String key);

	/**
	 * key-Object 存储
	 * 
	 * @throws IOException
	 */
	void setKey_Obj(String key, Object value, int expiration);

	/**
	 * 简单的Key-Object获取值
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Object getObjectByKey(String key) throws IOException, ClassNotFoundException;

	/**
	 * 根据key删除值
	 * 
	 * @throws IOException
	 */
	void del(String key);

	/**
	 * 过期处理
	 * 
	 * @param key
	 * @param time
	 */
	void expire(String key, Integer time);

	/**
	 * 批量key value获取
	 * 
	 * @param keys
	 * @return
	 */
	List<String> getValues(Collection<String> keys);

	Set<String> getKeys(String pattern);

	void setKey_Value(String s, String dayType);

	void batchDel(String redisKeyPre);

	boolean isLock(String key);

	boolean tryLock(String key, String value, int expireSeconds);

	boolean unlock(String key, String value);

}
