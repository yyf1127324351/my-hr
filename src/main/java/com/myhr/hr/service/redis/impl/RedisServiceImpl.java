package com.myhr.hr.service.redis.impl;

import com.myhr.hr.service.redis.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("redisService")
public class RedisServiceImpl implements RedisService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private ValueOperations<String, String> lockOperations;

	private ValueOperations<String, Object> valueOperations;

	private static final String UNLOCK_LUA_SCRIPT =
					"if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
					"then " +
					"    return redis.call(\"del\",KEYS[1]) " +
					"else " +
					"    return 0 " +
					"end ";

	@Override
	public void afterPropertiesSet() {
		this.lockOperations = stringRedisTemplate.opsForValue();
		this.valueOperations = redisTemplate.opsForValue();
	}

	@Override
	public void setKey_Value(String key, String value) {
		if (StringUtils.isNotBlank(key)) {
			this.valueOperations.set(key, value);
		}
	}

	/**
	 * 简单的key-value 存储
	 */
	@Override
	public void setKey_Value(String key, String value, int expiration) {
		if (StringUtils.isNotBlank(key)) {
			this.valueOperations.set(key, value, expiration, TimeUnit.SECONDS);
		}
	}

	/**
	 * 简单的Key-value获取值
	 */
	@Override
	public String getValueByKey(String key) {
		if (StringUtils.isNotBlank(key)) {
			return (String) this.valueOperations.get(key);
		}
		return null;
	}

	/**
	 * getValues
	 */
	@Override
	public List<String> getValues(Collection<String> keys) {
		if (CollectionUtils.isNotEmpty(keys)) {
			return this.valueOperations.multiGet(keys).stream().map(e -> e.toString()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public Set<String> getKeys(String pattern) {
		if(StringUtils.isEmpty(pattern)) {
			return Collections.emptySet();
		}
		return this.valueOperations.getOperations().keys(pattern);
	}

	/**
	 * key-Object 存储
	 *
	 */
	@Override
	public void setKey_Obj(String key, Object value, int expiration) {
		if (StringUtils.isNotBlank(key)) {
			this.valueOperations.set(key, value, expiration, TimeUnit.SECONDS);
		}
	}

	/**
	 * 简单的Key-Object获取值
	 *
	 */
	@Override
	public Object getObjectByKey(String key) {
		if (StringUtils.isNotBlank(key)) {
			return this.valueOperations.get(key);
		}
		return null;
	}

	/**
	 * 根据key删除值
	 * 
	 * @throws IOException
	 */
	@Override
	public void del(String key) {
		if (StringUtils.isNotBlank(key)) {
			this.valueOperations.getOperations().delete(key);
		}
	}

	@Override
	public void expire(String key, Integer time) {
		if (StringUtils.isNotBlank(key)) {
			this.valueOperations.getOperations().expire(key, time, TimeUnit.SECONDS);
		}
	}

	@Override
	public void batchDel(String pattern) {
		Set<String> keys = getKeys(pattern);
		if(CollectionUtils.isNotEmpty(keys)) {
			this.valueOperations.getOperations().delete(keys);
		}
	}

	@Override
	public boolean isLock(String key) {
		return this.lockOperations.get(getLockKey(key)) != null;
	}

	@Override
	public boolean tryLock(String key, String value, int expireSeconds) {
		if(StringUtils.isBlank(key)) {
			return false;
		}
		String lockKey = getLockKey(key);
		if(lockOperations.setIfAbsent(lockKey, value)){
			// 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
			try {
				lockOperations.getOperations().expire(lockKey, expireSeconds, TimeUnit.SECONDS);
				return true;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				lockOperations.getOperations().delete(lockKey);
			}
		}
		return false;
	}

	@Override
	public boolean unlock(String key, String lockValue) {
		if(StringUtils.isBlank(key)) {
			return false;
		}
		String lockKey = getLockKey(key);
		RedisCallback<Boolean> callback = (connection) -> connection.eval(
				UNLOCK_LUA_SCRIPT.getBytes(),
				ReturnType.BOOLEAN,
				1,
				lockKey.getBytes(StandardCharsets.UTF_8),
				lockValue.getBytes(StandardCharsets.UTF_8)
		);

		return stringRedisTemplate.execute(callback);
	}

	private String getLockKey(String key) {
		return "LOCK:" + key;
	}

}
