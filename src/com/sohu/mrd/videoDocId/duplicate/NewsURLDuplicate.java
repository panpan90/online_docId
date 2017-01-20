package com.sohu.mrd.videoDocId.duplicate;
import org.apache.log4j.Logger;
import com.sohu.mrd.videoDocId.constant.Constant;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.utils.MD5Utils;
/**
 * @author Jin Guopan
   @creation 2017年1月11日
 */
public class NewsURLDuplicate {

	public static final Logger LOG=Logger.getLogger(NewsURLDuplicate.class);
	public  String duplicatebyURL(String url,String url_index_name)
	{
		String flag=Constant.DUPLICATE_FLAG;
		String rowKey=MD5Utils.getMD5(url);
		String value=RedisCrud.get(url_index_name+"_"+rowKey);
		if(value!=null)
		{
			String[] values=value.split("\t", -1);
			String docId = values[0];
			String valueURL=values[1];
		    flag = docId;
		    //LOG.info("flag "+flag);
		    LOG.info("redis 通过url 排重成功"+"docId "+docId+"原始 url "+valueURL+"现在url "+url);
		}
		return flag;
	}
}
