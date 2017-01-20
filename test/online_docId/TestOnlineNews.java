package online_docId;
import java.util.HashMap;
import com.alibaba.fastjson.JSONObject;
import com.sohu.mrd.videoDocId.utils.HttpClientUtil;
/**
 * @author Jin Guopan
   @creation 2017年1月18日
 */
public class TestOnlineNews {
	public static void main(String[] args) {
		String onlineurl="http://localhost:8080/online_docId/getDocId";
		String removeeurl="http://10.16.44.237:8080/online_docId/getDocId";
		String url="http://www.sohu119.com";
		String title="我的标题";
		String content="我是内容";
		JSONObject  json=new JSONObject();
		json.put("url", url);
		json.put("title",title);
		json.put("content", content);
		json.put("category", "0"); //0 表示新闻排重
		json.put("introduce", "简介");
		json.put("isGroupImage", "1");//1 表示是组图新闻
		HashMap<String,String>  map=new HashMap<String,String>();
		map.put("json", json.toJSONString());
		String result=HttpClientUtil.doPost(removeeurl, map, "utf-8");
		System.out.println("result "+result);
	}
}
