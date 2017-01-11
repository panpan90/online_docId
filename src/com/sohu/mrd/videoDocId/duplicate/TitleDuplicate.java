package com.sohu.mrd.videoDocId.duplicate;
import org.apache.log4j.Logger;

import com.sohu.mrd.videoDocId.constant.Constant;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.utils.MD5Utils;
/**
   @author Jin Guopan
   @creation 2016年12月29日
 */
public class TitleDuplicate{
	private static final Logger LOG = Logger.getLogger(TitleDuplicate.class);
	public void excuteDuplicate(String title,String docId)
	{
		String  flag=duplicateByTitle(title,docId);
		if(flag.equals(Constant.TITLE_DUPLICATE_FLAG)) //没有重复的
		{
			buildTitleIndex(title,docId);
		}else{
			//LOG.info("通过标题重复的  "+title+" docId"+docId);
		}
	}
	
	private  String  duplicateByTitle(String title,String docId)
	{
		String flagDuplicate=Constant.TITLE_DUPLICATE_FLAG;
		String titleMD5=MD5Utils.getMD5(title);
		String redisKey = RedisConstant.KEY_PREFIX_VIDEO_TITLE_INDEX_TABLE+"#@"+titleMD5;
		String Redisvalue=RedisCrud.get(redisKey);
		if(Redisvalue!=null)
		{
			String[]  ss=Redisvalue.split("\t", -1);
			if(ss.length>=2)
			{
				String oldDocId=ss[0];
				if(oldDocId!=null)
				{
					flagDuplicate=oldDocId;
				}
			}
		}
		return flagDuplicate;
	}
	private void buildTitleIndex(String title,String docId)
	{
		String titleMD5=MD5Utils.getMD5(title);
		String redisKey = RedisConstant.KEY_PREFIX_VIDEO_TITLE_INDEX_TABLE+"#@"+titleMD5;
		long  time=System.currentTimeMillis();
		String Redisvalue = docId +"\t" +time;
		RedisCrud.set(redisKey, Redisvalue);
	}
}
