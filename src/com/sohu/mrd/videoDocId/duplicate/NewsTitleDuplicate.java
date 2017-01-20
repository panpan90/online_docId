package com.sohu.mrd.videoDocId.duplicate;
import org.apache.log4j.Logger;
import com.sohu.mrd.videoDocId.constant.Constant;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.utils.MD5Utils;
/**
 * @author   Jin Guopan
   @creation 2017年1月11日
 */
public class NewsTitleDuplicate {
	private static final Logger LOG = Logger.getLogger(NewsTitleDuplicate.class);
	public String duplicateByTitle(String title,String title_index_name)
	{
		String flag = Constant.DUPLICATE_FLAG;
		if(!title.trim().equals(""))
		{
			String titleMd5=MD5Utils.getMD5(title);
			String value=RedisCrud.get(title_index_name+"_"+titleMd5);
			if(value!=null)
			{
				String[] values=value.split("\001", -1);
				if(values.length>3)
				{
					String docId=values[0];
					String tempTitle=values[1];
					String tempURl=values[2];
					flag=docId;
					LOG.info("通过标题进行排重,原始标题为 "+title+" 索引库的标题为   "+tempTitle+" 索引库的url为  "+tempURl);
					return flag;
				}
			}
		}
		return flag;
	}
}
