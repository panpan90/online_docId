package com.sohu.mrd.videoDocId.constant;
/**
 * @author Jin Guopan
   @creation 2016年12月22日
   
     视频正式索引库
   public static final String  KEY_PREFIX_VIDEO_URL_INDEX_TABLE = "new_final_online_video_url_index_table";
   public static final String  KEY_PREFIX_VIDEO_CONTENT_INDEX_TABLE="new_final_online_video_content_index_table";
   public static final String  KEY_PREFIX_VIDEO_TITLE_INDEX_TABLE="new_final_online_video_title_index_table";
   新闻正式索引库
   	public static final String KEY_PREFIX_NEWS_URL_INDEX_TABLE="final_news_url_index_docId_table";
	public static final String KEY_PREFIX_NEWS_TITLE_INDEX_TABLE="final_news_title_index_docId_table";
	public static final String KEY_PREFIX_NEWS_content_INDEX_TABLE="final_news_content_index_docId_table";
  微信正式索引库
  public static final String  WEIXIN_PREFIX_URL_INDEX_TABLE="final_weixin_url_index_docId_table";
  public static final String  WEIXIN_PREFIX_TITLE_INDEX_TABLE="final_weixin_title_index_docId_table";
  public static final String  WEIXIN_PREFIX_CONTENT_INDEX_TABLE="final_weixin_content_index_docId_table";
 */
public class RedisConstant {
	//视频redis索引表
	public static final String  KEY_PREFIX_VIDEO_URL_INDEX_TABLE = "new_final_online_video_url_index_table";
	public static final String  KEY_PREFIX_VIDEO_CONTENT_INDEX_TABLE="new_final_online_video_content_index_table";
	public static final String  KEY_PREFIX_VIDEO_TITLE_INDEX_TABLE="new_final_online_video_title_index_table";
	//新闻相关的redis索引表
	public static final String KEY_PREFIX_NEWS_URL_INDEX_TABLE="news_url_index_docId_table";
	public static final String KEY_PREFIX_NEWS_TITLE_INDEX_TABLE="news_title_index_docId_table";
	public static final String KEY_PREFIX_NEWS_content_INDEX_TABLE="news_content_index_docId_table";
	//微信相关的索引表
	public static final String  WEIXIN_PREFIX_URL_INDEX_TABLE="final_weixin_url_index_docId_table";
	public static final String  WEIXIN_PREFIX_TITLE_INDEX_TABLE="final_weixin_title_index_docId_table";
	public static final String  WEIXIN_PREFIX_CONTENT_INDEX_TABLE="final_weixin_content_index_docId_table";
}
