package com.sohu.mrd.videoDocId.service;
import org.apache.log4j.Logger;

import com.sohu.mrd.videoDocId.constant.Constant;
import com.sohu.mrd.videoDocId.constant.RedisConstant;
import com.sohu.mrd.videoDocId.duplicate.NewsContentDuplicate;
import com.sohu.mrd.videoDocId.duplicate.NewsTitleDuplicate;
import com.sohu.mrd.videoDocId.duplicate.NewsURLDuplicate;
import com.sohu.mrd.videoDocId.model.NewsInfo;
import com.sohu.mrd.videoDocId.model.URLInfo;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.simhash.SimHasher;
import com.sohu.mrd.videoDocId.utils.HostDomainExtractKit;
import com.sohu.mrd.videoDocId.utils.KillPuctuation;
import com.sohu.mrd.videoDocId.utils.KillTag;
import com.sohu.mrd.videoDocId.utils.MD5Utils;
/**
 * @author Jin Guopan
   @creation 2017年1月11日
 */
public class GenerateNewsDocIdService{
	private static final  Logger LOG = Logger.getLogger(GenerateNewsDocIdService.class);
	private GenerateNewsDocIdService(){}
	private static class InstanceHolder{
		private static GenerateNewsDocIdService generateNewsDocIdService = new GenerateNewsDocIdService();
	}
	public static GenerateNewsDocIdService  getInstance()
	{
		return InstanceHolder.generateNewsDocIdService;
	}
	public   String  getDocId(NewsInfo newsInfo)
	{
		String docId ="";
		String url=newsInfo.getUrl();
		String title=newsInfo.getTitle();
		String content=newsInfo.getContent();
		boolean isGroupImage=newsInfo.isGroupImage();
		String introduce="";
		introduce=newsInfo.getIntroduce();
		String category=newsInfo.getCategory();
		String url_index_table="";
		String title_index_table="";
		String content_index_table="";
		if(category.equals("0"))
		{
			LOG.info("新闻docId start");
			url_index_table=RedisConstant.KEY_PREFIX_NEWS_URL_INDEX_TABLE;
			title_index_table=RedisConstant.KEY_PREFIX_NEWS_TITLE_INDEX_TABLE;
			content_index_table=RedisConstant.KEY_PREFIX_NEWS_content_INDEX_TABLE;
			LOG.info("索引表  url_index_table "+url_index_table+" title_index_table "+title_index_table+" content_index_table  "+content_index_table);
		}else if(category.equals("2"))
		{
			LOG.info("微信docId  start");
			url_index_table=RedisConstant.WEIXIN_PREFIX_URL_INDEX_TABLE;
			title_index_table=RedisConstant.WEIXIN_PREFIX_TITLE_INDEX_TABLE;
			content_index_table=RedisConstant.WEIXIN_PREFIX_CONTENT_INDEX_TABLE;
			LOG.info("索引表为  url_index_table  "+url_index_table+" title_index_table  "+title_index_table+" content_index_table "+content_index_table);
		}
		synchronized(GenerateNewsDocIdService.class)  //如果超时，取消同步
		{
			long urlStartTime=System.currentTimeMillis();
			//通过url排重
			NewsURLDuplicate  newsURLDuplicate = new NewsURLDuplicate();
			String urlFlag=newsURLDuplicate.duplicatebyURL(url,url_index_table);
			long urlEndTime=System.currentTimeMillis();
			LOG.info("url排重需要的时间  "+(urlEndTime-urlStartTime));
			if(urlFlag.equals(Constant.DUPLICATE_FLAG)) //url 没有重复的
			{
				long titleStartTime=System.currentTimeMillis();
				NewsTitleDuplicate  newTitleDuplicate = new NewsTitleDuplicate();
				String clearTitle=title.trim();
				String titleFlag=newTitleDuplicate.duplicateByTitle(clearTitle,title_index_table);
				long titleEndTime=System.currentTimeMillis();
				LOG.info("标题排重需要的时间  "+(titleEndTime-titleStartTime));
				if(titleFlag.equals(Constant.DUPLICATE_FLAG)) //标题没有重复的
				{
					long contentStartTime=System.currentTimeMillis();
					NewsContentDuplicate newsContentDupicate = new NewsContentDuplicate();
					String realContent=introduce+content;
					String pureContent=KillPuctuation.killPuctuation(KillTag.killTags(realContent)) ;
					SimHasher simHasher = new SimHasher(pureContent);
					String simHash =simHasher.getHash();
					String contentFlag=newsContentDupicate.duplicateByContent(simHash, clearTitle,content_index_table);
					long contentEndTime=System.currentTimeMillis();
					long contentTime = (contentEndTime-contentStartTime);
					LOG.info("内容排重需要的时间 "+contentTime+" url "+url);
					if(contentFlag.equals(Constant.DUPLICATE_FLAG))//没有重复的
					{
						long newDocIdStartTime = System.currentTimeMillis();
						//产生docId
						String newDocId=generateDocId(url);
						//建立url索引
						buildURLIndex(url, newDocId,url_index_table);
						//构建标题索引
						buildTitleIndex(clearTitle, newDocId, url,title_index_table);
						//建立simhash索引
						buildContenIndex(newDocId, simHash, clearTitle,url,content_index_table);
						long newDocIdEndTime = System.currentTimeMillis();
						LOG.info("新建docId 需要的时间  "+(newDocIdEndTime-newDocIdStartTime));
						docId = newDocId;
						return docId;
					}else{//通过内容相似进行的排重
						docId=contentFlag;
						buildURLIndex(url, docId,url_index_table);
						buildTitleIndex(clearTitle, docId, url,title_index_table);
						LOG.info("通过content 进行排重的url 为 "+url);
						return docId;
					}
				}else{ //标题重复
					docId=titleFlag;
					LOG.info("通过title重复排重  要排重的原始url为"+url);
					return docId;
				}
			}else{
				docId=urlFlag;
				return docId;
			}
		}
	}
	//构建标题 docId索引
	private void buildTitleIndex(String title,String docId,String url,String title_index_name)
	{
		String titleMd5=MD5Utils.getMD5(title);
		long  storageTime = System.currentTimeMillis();
		String rediskey=title_index_name+"_"+titleMd5;
		String value=docId+"\001"+title+"\001"+url+"\001"+storageTime;
		RedisCrud.set(rediskey, value);
	}
	//构建 url docId索引
	private void buildURLIndex(String url,String docId,String url_index_name)
	{
	    String urlMD5=MD5Utils.getMD5(url);
	    long  storageTime=System.currentTimeMillis();
	    String rediskey=url_index_name+"_"+urlMD5;
	    String value=docId+"\t"+url+"\t"+storageTime;
	    RedisCrud.set(rediskey, value);
	}
	
	//构建 contentSimhash索引
	private void buildContenIndex(String docId,String simHash,String title,String url,String content_index_name)
	{
		long storageTime=System.currentTimeMillis();
		String simHashFragment1 = simHash.substring(0, 16);
		String simHashFragment2 = simHash.substring(16, 32);
		String simHashFragment3 = simHash.substring(32, 48);
		String simHashFragment4 = simHash.substring(48, 64);
		
		String redisKey1=content_index_name+"_"+simHashFragment1;
		String redisKey2=content_index_name+"_"+simHashFragment2;
		String redisKey3=content_index_name+"_"+simHashFragment3;
		String redisKey4=content_index_name+"_"+simHashFragment4;
		
		String value=docId+"\001"+title+"\001"+simHash+"\001"+url+"\001"+storageTime;
		RedisCrud.set2list(redisKey1, value);
		RedisCrud.set2list(redisKey2, value);
		RedisCrud.set2list(redisKey3, value);
		RedisCrud.set2list(redisKey4, value);
	}
	
	private  String generateDocId(String url){
		URLInfo urlInfo = extractURLHost(url);
		String host = urlInfo.getHost();
		String domain = urlInfo.getDomain();
		String urlMD5=MD5Utils.getMD5(url);
		String hostMD5=MD5Utils.getMD5(host);
		String domainMD5=MD5Utils.getMD5(domain);
		String md5=MD5Utils.getMD5("news"+urlMD5+hostMD5+domainMD5);
		StringBuilder docIdSB=new StringBuilder(md5);
		String docId=docIdSB.insert(16, "-").toString();
		return docId;
	}
	/**
	 * 抽取主机名和顶级域名
	 * @param url
	 * @return
	 */
	private URLInfo extractURLHost(String url){
		URLInfo urlInfo = new URLInfo();
		String host = HostDomainExtractKit.extractURLHost(url);
		String domain = HostDomainExtractKit.getTopLevelDomain(url);
		urlInfo.setHost(host);
		urlInfo.setDomain(domain);
		return urlInfo;
	}
}
