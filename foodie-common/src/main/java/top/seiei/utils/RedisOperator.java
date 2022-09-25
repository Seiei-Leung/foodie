package top.seiei.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Title: Redis 工具类
 * @author 慕课网
 */
@Component
public class RedisOperator {

	/**
	 * 操作 Redis 的接口
	 */
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	// Key（键），简单的key-value操作

	/**
	 * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key 键名
	 * @return
	 */
	public long ttl(String key) {
		return redisTemplate.getExpire(key);
	}
	
	/**
	 * 实现命令：expire 设置过期时间，单位秒
	 * 
	 * @param key 键名
	 * @return
	 */
	public void expire(String key, long timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 实现命令：INCR key，增加key一次
	 * 
	 * @param key 键名
	 * @return
	 */
	public long incr(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 实现命令：DEL key，删除一个key
	 * 
	 * @param key 键名
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}

	// String（字符串）

	/**
	 * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
	 * 
	 * @param key 键名
	 * @param value 数值
	 */
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
	 * 
	 * @param key 键名
	 * @param value 数值
	 * @param timeout （以秒为单位）
	 */
	public void set(String key, String value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 实现命令：GET key，返回 key所关联的字符串值。
	 * 
	 * @param key 键名
	 * @return
	 */
	public String get(String key) {
		return (String)redisTemplate.opsForValue().get(key);
	}

	/**
	 * 批量查询，对应 mget
	 * @param keys 键名
	 * @return
	 */
	public List<String> mget(List<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 批量查询，管道pipeline
	 * 作用就是持久化连接类似于 Nginx 中的 keepalive 属性，增大吞吐量
	 * @param keys 键名列表
	 * @return
	 */
	public List<Object> batchGet(List<String> keys) {
		// 重写 doInRedis
		List<Object> result = redisTemplate.executePipelined(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnection src = (StringRedisConnection)connection;
				for (String k : keys) {
					src.get(k);
				}
				return null;
			}
		});
		return result;
	}


	// Hash（哈希表）

	/**
	 * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
	 * 
	 * @param key 键名
	 * @param field 属性名
	 * @param value 数值
	 */
	public void hset(String key, String field, Object value) {
		redisTemplate.opsForHash().put(key, field, value);
	}

	/**
	 * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
	 *
     * @param key 键名
     * @param field 属性名
	 * @return
	 */
	public String hget(String key, String field) {
		return (String) redisTemplate.opsForHash().get(key, field);
	}

	/**
	 * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 *
     * @param key 键名
     * @param fields 属性名
	 */
	public void hdel(String key, Object... fields) {
		redisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
	 * 
	 * @param key  键名
	 * @return
	 */
	public Map<Object, Object> hgetall(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	// List（列表）

	/**
	 * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
	 * 
	 * @param key  键名
	 * @param value 数值
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long lpush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 实现命令：LPOP key，移除并返回列表 key的头元素。
	 * 
	 * @param key  键名
	 * @return 列表key的头元素。
	 */
	public String lpop(String key) {
		return (String)redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
	 * 
	 * @param key  键名
	 * @param value 数值
	 * @return 执行 LPUSH命令后，列表的长度。
	 */
	public long rpush(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

}