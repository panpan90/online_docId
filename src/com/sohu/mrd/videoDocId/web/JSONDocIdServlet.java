package com.sohu.mrd.videoDocId.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sohu.mrd.videoDocId.model.FilterReason;
import com.sohu.mrd.videoDocId.model.NewsInfo;
import com.sohu.mrd.videoDocId.redis.RedisCrud;
import com.sohu.mrd.videoDocId.service.GenerateDocIdServiceByRedis;
import com.sohu.mrd.videoDocId.service.GenerateNewsDocIdService;
import com.sohu.mrd.videoDocId.utils.HttpClientUtil;
/**
 * @author   Jin Guopan
 * @version 2016-12-16
 */
public class JSONDocIdServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(JSONDocIdServlet.class);
	private static final String CACH_PREFIX="online_new_docId_result_cach_";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String json = request.getParameter("json"); //接受前端 json 数据
		boolean jsonFlag=true;
		JSONObject result = new JSONObject();
		result.put("status", "success");
		if (json.trim().equals("") || json ==null) {
			result.put("status", "fail");
			result.put("msg", "json is null");
		} else {
			String category = "";
			String url = "";
			String source = "";
			String content = "";
			String content_length =""; 
			String title_length = "";
			String media ="";
			String publish_time = "";
			String title ="";
			String introduce="";
			String sort="";
			String isGroupImage="";
			String imageCount="";
			try {
				 JSONObject jsonObject = JSON.parseObject(json);
				 category = jsonObject.getString("category");
				 url = jsonObject.getString("url");
				 source = jsonObject.getString("source");
				 content = jsonObject.getString("content");
				 content_length = jsonObject.getString("content_length");
				 title_length = jsonObject.getString("title_length");
				 media = jsonObject.getString("media");
				 publish_time = jsonObject.getString("publish_time");
				 title = jsonObject.getString("title");
				 introduce=jsonObject.getString("introduce");
				 sort=jsonObject.getString("sort");
				 isGroupImage=jsonObject.getString("isGroupImage");
				 imageCount=jsonObject.getString("imageCount");
			} catch (Exception e) {
				jsonFlag=false;
				LOG.error("前端 json解析错误 ",e);
			}
			LOG.info(" title"+title+"\t"+"url "+url+"category "+category+"introduce "+
					introduce+" sort "+sort+" isGroupImage"+isGroupImage +" media "+media+" imageCount "+imageCount);
			if(category.equals("1"))
			{
				LOG.info("视频内容为 "+content);
			}
			if(jsonFlag)
			{
				if(!url.trim().equals("")&&!title.trim().equals(""))
				{
					if(!category.trim().equals(""))
					{
						if(category.equals("1")) //视频排重
						{
							LOG.info("category 为 "+category+" 视频docId start");
							GenerateDocIdServiceByRedis  generateDocIdService =GenerateDocIdServiceByRedis.getInstance();
							String docId=generateDocIdService.getDocId(url, title, content);
							result.put("docId", docId);
						}else if(category.equals("0") ||category.equals("2") )//0 新闻排重 2 表示微信
						{
							GenerateNewsDocIdService  generateNewsDocIdService = GenerateNewsDocIdService.getInstance();
							NewsInfo newsInfo = new NewsInfo();
							newsInfo.setCategory(category);
							newsInfo.setContent(content);
							boolean groupImage = false;
							if(isGroupImage!=null&&isGroupImage.equals("1"))
							{
								groupImage=true;
							}
							newsInfo.setGroupImage(groupImage);
							newsInfo.setUrl(url);
							newsInfo.setTitle(title);
							newsInfo.setIntroduce(introduce);
							if(imageCount!=null && media!=null)
							{
								    String cachResult=RedisCrud.get(CACH_PREFIX+url);
								    if(cachResult==null)
								    {
								    	long docIdStartTime=System.currentTimeMillis();
								    	String docId=generateNewsDocIdService.getDocId(newsInfo);
								    	long docIdEndTime=System.currentTimeMillis();
								    	LOG.info("docId 排重需要的时间  "+(docIdEndTime-docIdStartTime));
										FilterReason  filterReason=filterNews(url, title, content, sort, imageCount, media);
										result.put("filterStatus", filterReason.getStatus());
										result.put("filterReason", filterReason.getReason());
										result.put("docId", docId);
										//把超时的放入缓存里面
										RedisCrud.setCach(CACH_PREFIX+url, result.toJSONString());
								    }else{
								    	//从缓存中取得结果
								    	LOG.info("[从缓存中取得结果]向前端返回的结果  reslut "+cachResult);
								    	LOG.info("url "+url+" title "+title);
										write2Client(cachResult,response);
										return;
								    }
									
							}else{
								result.put("status", "fail");
								result.put("msg", "imageCount or media is null");
							}
						}
					}else{
						// url 或者 title 为空
						result.put("status", "fail");
						result.put("msg", "category is empty");
					}
				}else{
					// url 或者 title 为空
					result.put("status", "fail");
					result.put("msg", "url or title is empty");
				}
			}else{
				//json解析不正常
				result.put("status", "fail");
				result.put("msg", "JSONException");
			}
		}
		LOG.info("向前端返回的结果  reslut "+result.toJSONString());
		write2Client(result.toJSONString(),response);
	}
	/**
	 * 向前端写入数据
	 * @throws IOException
	 */
	private void write2Client(String vlaue, HttpServletResponse response) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(vlaue);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * http://10.16.44.236:8080/text_classification/getNewsClassification
	 * http://10.16.44.239:8080/text_classification/getNewsClassification
	 */
	public FilterReason   filterNews(String url,String title,String content,String sort,String imageCount,String media){
		String distanceURL="http://10.16.44.236:8080/text_classification/getNewsClassification";
		HashMap<String,String>  map=new HashMap<String,String>();
		map.put("title", title);
		map.put("url", url);
		map.put("content", content);
		map.put("sort", sort);
		map.put("imageCount", imageCount);
		map.put("media", media);
		String result=HttpClientUtil.doPost(distanceURL, map, "utf-8");
		JSONObject  json=JSONObject.parseObject(result);
		FilterReason filterReason= new FilterReason();
		String reson=json.getString("reason");
		String status=json.getString("status");
		LOG.info("236 接口 过滤接口返回的内容  "+result);
		filterReason.setReason(reson);
		filterReason.setStatus(status);
		return filterReason;
	}
}
