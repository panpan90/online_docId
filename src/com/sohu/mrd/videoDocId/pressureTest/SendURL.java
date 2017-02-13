package com.sohu.mrd.videoDocId.pressureTest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sohu.mrd.videoDocId.service.GenerateDocIdServiceByRedis;
import com.sohu.mrd.videoDocId.utils.FileKit;
import com.sohu.mrd.videoDocId.utils.HttpClientUtil;
public class SendURL {
	public  static final Logger LOG=Logger.getLogger(SendURL.class);
	public static void main(String[] args) throws Exception {
		String urldistance="http://10.10.26.125:8088/summary/getsummary";
		String urldistance2="http://10.10.27.90:8080/summary/getsummary";
		String urldistance3="http://10.10.120.135:8080/summary/getsummary";
		String local="http://localhost:8080/online_docId/getDocId";
		String ngix="http://service1.mrd.sohu.com/summary/getsummary";
		String ngixDocId="http://10.10.120.135:8080/online_docId/getDocId";
		String ngixDocId5="http://10.16.44.237:8080/online_docId/getDocId";
		String ngixDocId6="http://10.16.44.236:8080/online_docId/getDocId";
		String ngixDocId8="http://10.16.44.238:8080/online_docId/getDocId";
		String ngixDocId11="http://service1.mrd.sohuno.com/online_docId/getDocId";
		JSONObject  json=new JSONObject();
		json.put("url", "http://www.baidu.com/xxx");
		json.put("title", "标题现货");
		json.put("content", "内容adfafda内容内容sdfsasd");
		json.put("category", "0");//新闻
		json.put("sort", "41");
		json.put("media", "sohu");
		json.put("imageCount", "9");
		HashMap<String,String>  map=new HashMap<String,String>();
		map.put("json", json.toJSONString());
		long startTime=System.currentTimeMillis();
		String result=HttpClientUtil.doPost(local, map, "utf-8");
		long endTime=System.currentTimeMillis();
		System.out.println("时间为 "+(endTime-startTime));
		LOG.info("result "+result);
//		for(int i=0;i<50;i++)
//		{
//			JSONObject  json=new JSONObject();
//			json.put("url", "http://www.baidu.com"+i);
//			json.put("title", "  我是标题"+i);
//			json.put("content", "内容");
//			HashMap<String,String>  map=new HashMap<String,String>();
//			map.put("json", json.toJSONString());
//			long startTime=System.currentTimeMillis();
//			String result=HttpClientUtil.doPost(ngixDocId, map, "utf-8");
//			long endTime=System.currentTimeMillis();
//			System.out.println("时间为 "+(endTime-startTime));
//			LOG.info("result "+result);
//		}
//		JSONObject  resultJson=JSON.parseObject(result);
//		String docId= resultJson.getString("docId");
//		LOG.info("docId "+docId);
	}
	
	@Test
	public void timeOut()
	{
		String ngixDocId8="http://10.16.44.238:8080/online_docId/getDocId";
		String local="http://localhost:8080/online_docId/getDocId";
	  //String title="体育招聘|体育大生意新年第一波招聘!另有北上广12家公司48个岗位";
	  String title="早读|每天三分钟 尽览现货事(2月10日)测试标题";
	  //String contentURL= "http://service.k.sohu.com/server/interface/readinfo.go?aid=1001&date=2013-07-25&ids=176733764_1&token=9e2e0350c8f8aa0db78ef6cbe70dc6fa";
	  long contentTimeStart=System.currentTimeMillis();
	  String url="http://service.k.sohu.com/server/interface/jinguopan/readinfo.go?aid=1001&date=2013-07-25&ids=176726199_1&token=9e2e0350c8f8aa0db78ef6cbe70dc6fa";
	  String contentURL="http://service.k.sohu.com/server/interface/readinfo.go?aid=1001&date=2013-07-25&ids=176726199_1&token=9e2e0350c8f8aa0db78ef6cbe70dc6fa";
	  String content=HttpClientUtil.executeGet(contentURL);
	  long contentTimeEnd=System.currentTimeMillis();
	  System.out.println("contentTiem "+(contentTimeEnd-contentTimeStart));
	  JSONObject  json=new JSONObject();
	  json.put("url", url);
	  json.put("title", title);
	  json.put("content", content);
	  json.put("category", "0");//新闻
	  json.put("sort", "41");
	  json.put("media", "sohu");
	  json.put("imageCount", "9");
		HashMap<String,String>  map=new HashMap<String,String>();
		map.put("json", json.toJSONString());
		long startTime=System.currentTimeMillis();
		String result=HttpClientUtil.doPost(ngixDocId8, map, "utf-8");
		long endTime=System.currentTimeMillis();
		System.out.println("时间为 "+(endTime-startTime));
		LOG.info("result "+result);
	}
	
	@Test
	public void testRedis()
	{
		String myurl="http://localhost:8080/online_docId/getDocId";
		long all_start_Time = System.currentTimeMillis();
		try {
			FileInputStream fis = new FileInputStream("data/kafkaData");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temp="";
			while((temp=br.readLine())!=null)
			{
				if(!temp.trim().equals(""))
				{
					String[] temps=temp.split("\t", -1);
					String url=temps[0];
					String title=temps[1];
					String content=temps[2];
					long startTime = System.currentTimeMillis();
//					GenerateDocIdServiceByRedis generateDocIdServiceByRedis = GenerateDocIdServiceByRedis.getInstance();
//					String docId=generateDocIdServiceByRedis.getDocId(url, title, content);
					JSONObject  json=new JSONObject();
					json.put("url", url);
					json.put("title",title);
					json.put("content", content);
					HashMap<String,String>  map=new HashMap<String,String>();
					map.put("json", json.toJSONString());
					String result=HttpClientUtil.doPost(myurl, map, "utf-8");
					JSONObject  resultJson=JSON.parseObject(result);
					String docId=resultJson.getString("docId");
					long endTime = System.currentTimeMillis();
					StringBuilder sb = new StringBuilder();
					sb.append(docId);
					sb.append("\t");
					sb.append(url);
					sb.append("\t");
					sb.append(title);
					sb.append("\t");
					sb.append(content);
					sb.append("\t");
					sb.append(endTime-startTime);
					sb.append("\n");
					String writeStr=sb.toString();
					FileKit.write2File(writeStr, "data/newredisResult_2016_1_4_5", true);
					System.out.println(writeStr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long all_end_time=System.currentTimeMillis();
		LOG.info("总耗时  "+(all_end_time-all_start_Time)/1000+"s");
	}
}
