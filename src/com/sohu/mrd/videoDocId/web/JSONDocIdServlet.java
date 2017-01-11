package com.sohu.mrd.videoDocId.web;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sohu.mrd.videoDocId.service.GenerateDocIdServiceByRedis;
/**
 * @author   Jin Guopan
 * @version 2016-12-16
 */
public class JSONDocIdServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(JSONDocIdServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String json = request.getParameter("json"); //接受前端 json 数据
		boolean jsonFlag=true;
		LOG.info("前端接受的 json为  "+json);
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
			} catch (Exception e) {
				jsonFlag=false;
				LOG.error("前端接受的json解析错误 ",e);
			}
			LOG.info("前端接受的参数  title"+title+"\t"+"content"+content+"url "+url);
			LOG.info("解析后json的数据 category"+category+" url"+url+"source "+source+"content "+content
					+"content_length "+content_length+" title_length "+title_length +" media "+media 
					+"publish_time "
					+publish_time +"title "+title);
			if(jsonFlag)
			{
				if(!url.trim().equals("")&&!title.trim().equals(""))
				{
					GenerateDocIdServiceByRedis  generateDocIdService =GenerateDocIdServiceByRedis.getInstance();
					String docId=generateDocIdService.getDocId(url, title, content);
					result.put("docId", docId);
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
	public void write2Client(String vlaue, HttpServletResponse response) {
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
}
