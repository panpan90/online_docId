package com.sohu.mrd.videoDocId.duplicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.sohu.mrd.videoDocId.constant.Constant;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.model.NewSimilaryInfo;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.utils.JaccardSimilaryKit;
import com.sohu.mrd.videoDocId.utils.SimHashKit;
/**
 * @author Jin Guopan
   @creation 2017年1月11日
 */
public class NewsContentDuplicate {
   private static final Logger LOG = Logger.getLogger(NewsContentDuplicate.class);
   /**
    * 通过内容进行排重
    * @param simhash
    * @param content
    * @return
    */
   public String duplicateByContent(String simhash,String title,String content_index_name)
   {
	    String flag=Constant.DUPLICATE_FLAG;
	    String simhashFragment1=simhash.substring(0, 16);
		String simhashFragment2=simhash.substring(16, 32);
		String simhashFragment3=simhash.substring(32, 48);
		String simhashFragment4=simhash.substring(48, 64);
		String redisKey1=content_index_name+"_"+simhashFragment1;
		String redisKey2=content_index_name+"_"+simhashFragment2;
		String redisKey3=content_index_name+"_"+simhashFragment3;
		String redisKey4=content_index_name+"_"+simhashFragment4;
		List<String> simhashValues1=RedisCrud.getList(redisKey1);
		List<String> simhashValues2=RedisCrud.getList(redisKey2);
		List<String> simhashValues3=RedisCrud.getList(redisKey3);
		List<String> simhashValues4=RedisCrud.getList(redisKey4);
		List<String> simdocIds=new ArrayList<String>();
	    if(simhashValues1!=null)
	    {
	    	for(int i=0;i<simhashValues1.size();i++)
	    	{
	    		String docId_title_time=simhashValues1.get(i);
	    		if(docId_title_time!=null&&!docId_title_time.trim().equals(""))
	    		{
	    			simdocIds.add(docId_title_time);
	    		}
	    		
	    	}
	    }
	    
	    if(simhashValues2!=null)
	    {
	    	for(int i=0;i<simhashValues2.size();i++)
	    	{
	    		String docId_title_time=simhashValues2.get(i);
	    		if(docId_title_time!=null&&!docId_title_time.trim().equals(""))
	    		{
	    			simdocIds.add(docId_title_time);
	    		}
	    	}
	    }
	    
	    if(simhashValues3!=null)
	    {
	    	for(int i=0;i<simhashValues3.size();i++)
	    	{
	    		String docId_title_time=simhashValues3.get(i);
	    		if(docId_title_time!=null&&!docId_title_time.trim().equals(""))
	    		{
	    			simdocIds.add(docId_title_time);
	    		}
	    	}
	    }
	    
	    if(simhashValues4!=null)
	    {
	    	for(int i=0;i<simhashValues4.size();i++)
	    	{
	    		String docId_title_time=simhashValues4.get(i);
	    		if(docId_title_time!=null&&!docId_title_time.trim().equals(""))
	    		{
	    			simdocIds.add(docId_title_time);
	    		}
	    	}
	    }
	    List<NewSimilaryInfo>  newsSimilaryInfos= getSimilaryInfo(simdocIds, title, simhash);
	   if(newsSimilaryInfos.size()==0)
	   {
		   return flag;
	   }else{
		   TreeMap<String, String> treeMap = new TreeMap<String, String>();
		   for(int i=0;i<newsSimilaryInfos.size();i++)
		   {
			   StringBuilder keySb = new StringBuilder();
			   NewSimilaryInfo  newSimilaryInfo= newsSimilaryInfos.get(i);
			   Double titleSimilary=newSimilaryInfo.getTitleJaccard();
			   Integer distance= newSimilaryInfo.getHanmingDistance();
			   String docId= newSimilaryInfo.getDocId();
			   String tempURl=newSimilaryInfo.getUrl();
			   int subDistance=10-distance;
			   keySb.append(titleSimilary);
			   keySb.append("&");
			   keySb.append(subDistance);
			   keySb.append("&");
			   keySb.append(i);
			   keySb.append("&");
			   keySb.append(tempURl);
			   treeMap.put(keySb.toString(), docId);
		   }
		   //取得最相似的那个docId
		   Entry<String, String> entry=treeMap.lastEntry();
		   String docId=entry.getValue();
		   String key=entry.getKey();
		   if(docId!=null&&!docId.trim().equals(""))
		   {
			   flag=docId;
			   LOG.info("通过content排重成功  titleSimilary subDistance  i  url 分别为  "+key);
		   }
	   }
	   return flag;
   }
   
   public List<NewSimilaryInfo> getSimilaryInfo(List<String> simdocIds,String title,String simhash)
   {
	   List<NewSimilaryInfo> newsSimilaryInfos=new ArrayList<NewSimilaryInfo>();
	   if(simdocIds!=null&&simdocIds.size()>0)
	    {
	    	for(int i=0;i<simdocIds.size();i++)
	    	{
	    		String docId_title_time=simdocIds.get(i);
	    		String[] docId_title_times=docId_title_time.split("\001", -1);
	    		if(docId_title_times.length>2)
	    		{
	    			String tempdocId=docId_title_times[0];
	    			String temptitle=docId_title_times[1];
	    			String tempsimhash=docId_title_times[2];
	    			String tempurl=docId_title_times[3];
	    			double titleJaccardsimilary=JaccardSimilaryKit.getJaccardSimilarity(temptitle, title);
	    			if(titleJaccardsimilary>0.85)
	    			{
	    				int hanmingDistance=SimHashKit.getHanMingDistance(simhash, tempsimhash);
	    				if(hanmingDistance<=3) //标题相似，内容的海明距离小于3判断为相似
	    				{
	    					NewSimilaryInfo info=new NewSimilaryInfo();
	    					info.setHanmingDistance(hanmingDistance);
	    					info.setTitleJaccard(titleJaccardsimilary);
	    					info.setDocId(tempdocId);
	    					info.setUrl(tempurl);
	    					newsSimilaryInfos.add(info);
	    				}
	    			}
	    		}
	    	}
	    }
	   
	   return newsSimilaryInfos;
   }
}
