package com.sohu.mrd.videoDocId.redis;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sohu.mrd.framework.redis.client.client.CodisLocalClient;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.utils.MD5Utils;
/**
 * @author Jin Guopan
   @creation 2016年12月22日
   redis相关的操作 list 和 zset的使用。
 */
public class RedisCrud {
	private static CodisLocalClient codisLocalClient=RedisConnection.codisLocalClient;
	/**
	 * value 是个 string 类型
	 * @param key
	 * @param value
	 */
	public static  void set(String key,String value)
	{
		codisLocalClient.set(key, value);
	}
	
	/**
	 * 设置缓存set
	 * @param key
	 * @return
	 */
	public static void setCach(String key,String value)
	{
		codisLocalClient.set(key,value);
		codisLocalClient.expire(key, 60*60*24*7);//过期时间为1个星期
	}
	public static String  get(String key)
	{
		return codisLocalClient.get(key);
	}
	/**
	 * value 是个 list
	 * @param key
	 * @param value
	 */
	public static void set2list(String key,String value)
	{
		codisLocalClient.lpush(key, value);
	}
	public static List<String> getList(String key)
	{
		List<String>  list=codisLocalClient.lrange(key, 0, -1);
		return list;
	}
	/**
	 * value 是个 set 集合 
	 * @param key
	 * @param value
	 */
	public static void put2set(String key,String value)
	{
		codisLocalClient.sadd(key, value);
	}
	public static Set<String>  getSet(String key)
	{
		return codisLocalClient.smembers(key);
	}
	/**
	 *  value是 hash 即 hashMap 集合
	 */
	public static void put2hash(String key,String hashKey,String hashValue)
	{
		codisLocalClient.hset(key, hashKey, hashValue);
	}
	public static String getHash(String key,String hashKey)
	{
		return codisLocalClient.hget(key, hashKey);
	}
	/**
	 * value 是 zset 集合 
	 */
	public static void put2zset(String key,String value,Double score)
	{
		codisLocalClient.zadd(key, score, value);
	}
	public static Set<String> getZset(String key)
	{
		Set<String>  set=codisLocalClient.zrange(key, 0,-1);//zrange:显示集合中指定下标的元素值(按score从小到大排序)
		return set;
	}
	/**
	 * 批量删除keys
	 * @param pattern
	 */
	public static void  deleteKeys(String pattern)  //公司集群不支持keys，只能对key设置过期时间
	{
		Set<String> keys=codisLocalClient.keys(pattern);
		if(keys==null || keys.size()<=0)
		{
			return;
		}
		String[] keystrs=(String[]) keys.toArray();
		codisLocalClient.del(keystrs);
	}
	public static void main(String[] args){
//		RedisCrud.deleteKeys(RedisConstant.KEY_PREFIX_NEWS_URL_INDEX_TABLE+"*");
//		RedisCrud.deleteKeys(RedisConstant.KEY_PREFIX_NEWS_content_INDEX_TABLE+"*");
//		String value=codisLocalClient.get(RedisConstant.KEY_PREFIX_NEWS_URL_INDEX_TABLE+"_"+"f03f5717616221de41881be555473a02");
//		System.out.println("value "+value);
////		
//		Set<String>  set=codisLocalClient.keys(RedisConstant.KEY_PREFIX_NEWS_URL_INDEX_TABLE+"_"+"f03f5717616221de41881be555473a02");
//		System.out.println("set.size() "+set.size());
//		RedisCrud.put2zset("test_zset", "1", 0.8);
//		RedisCrud.put2zset("test_zset", "2", 0.3);
//		RedisCrud.put2zset("test_zset", "3", 0.5);
//		RedisCrud.put2zset("test_zset", "4", 0.7);
//		RedisCrud.put2zset("test_zset", "8", 0.9);
//		Set<String> set=RedisCrud.getZset("test_zset");
//		if(set.size()>0)
//		{
//			System.out.println(set);
//		}
//		RedisCrud.put2hash("userId1", "name", "jinguopan");
//		RedisCrud.put2hash("userId1", "age", "22");
//		RedisCrud.put2hash("userId1", "address", "shanxi_yongji");
//		String value=RedisCrud.getHash("userId1", "name");
//		System.out.println("value "+value);
		
//		RedisCrud.set2list("test_table_url_index_10001", "123");
//		RedisCrud.set2list("test_table_url_index_10001", "456");
//		RedisCrud.set2list("test_table_url_index_10001", "789");
//		RedisCrud.set2list("test_table_url_index_10002", "123");
//		RedisCrud.set2list("test_table_url_index_10002", "456");
//		RedisCrud.set2list("test_table_url_index_10002", "789");
//		List<String>  list1=RedisCrud.getList("test_table_url_index_10001");
//		List<String>  list2=RedisCrud.getList("test_table_url_index_10002");
//		List<String>  list3=RedisCrud.getList("test_table_url_index_10003");
//		for(int i=0;i<list1.size();i++)
//		{
//			System.out.println(list1.get(i));
//		}
//		System.out.println(list1.toString());
//		System.out.println(list1.size());
//		System.out.println(list2.size());
//		System.out.println(list3.size());
		//redis 是 key value　结构　　list 和  set 是相对value而言。
//		RedisCrud.set2list("test_table_url_index_100014", "123");
//		RedisCrud.set2list("test_table_url_index_100014", "123");
//		RedisCrud.set2list("test_table_url_index_100014", "456");
//		RedisCrud.set2list("test_table_url_index_100014", "456");
//		List<String> list= RedisCrud.getList("test_table_url_index_100014");
//		System.out.println("list"+list.toString());
//		RedisCrud.put2set("test_table_url_index_100013", "123");
//		RedisCrud.put2set("test_table_url_index_100013", "123");
//		RedisCrud.put2set("test_table_url_index_100013", "456");
//		RedisCrud.put2set("test_table_url_index_100013", "456");
//		Set<String>  set1= RedisCrud.getSet("test_table_url_index_100013");
//		System.out.println(set1.toString());
//		Set<String> mohuSet=codisLocalClient.keys("test_table_url_index*");
//		if(mohuSet!=null)
//		{
//			System.out.println("模糊查找 "+mohuSet.size());
//		}else{
//			System.out.println("没有查找到");
//		}
//		List<String>   result=RedisCrud.getList("test_table_url_index_100014");
//		Set<String>  contentSet=codisLocalClient.keys("video_content_index_table*");
//		if(contentSet!=null)
//		{
//			System.out.println(contentSet.size());
//		}
//		Set<String> urlSet=codisLocalClient.keys("video_url_index_table*");
//		if(urlSet!=null)
//		{
//			System.out.println("urlSet "+urlSet);
//		}
//		
//		Set<String> testUrlSet=codisLocalClient.keys("test_table_url_index_100013");
//		System.out.println("testUrlSet "+testUrlSet.size());
	}
}
