//package com.sohu.mrd.preprocess.common.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.CoreConnectionPNames;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//
//import com.sohu.mrd.preprocess.common.object.MrdNewsObject;
//import com.sohu.mrd.preprocess.common.object.MrdNewsObject.NewsGroupResult2;
//import com.sohu.mrd.preprocess.common.object.MrdNewsObject.NewsGroupResult3;
//
//public class TitlePartyService {
//
//	/**
//	 * @param args
//	 */
//	
//	private static final Log LOG = LogFactory.getLog(TitlePartyService.class);
//	
//	public static void setNewsTitleParty(MrdNewsObject news) {
// 
//		
//		int mode = 0;
//		if (news == null) {
//			LOG.error("[F-C-01] Error in null news TitlePartyService object");
//			return;
//		}
//		if (news.getNid() == null || news.getNid().equals("")) {
//			return;
//		}
//		   String result = "-1";
//		    
//		  String title = news.getT();
//		  String co = news.getCo();
//
//		   HttpClient httpclient = new DefaultHttpClient();
//		   httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 600); 
//		   httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 600);
//		
//		   String uriAPI = "http://service1.mrd.sohuno.com/news_classifier_server/predict?name=biaotidang";
//		   HttpPost httppost = new HttpPost(uriAPI); 			
//		LOG.info("[F-C-01] Begin to req TitlePartyService , oid:" + news.getOid() + " nid:" + news.getNid() + " subName:" + news.getSubName());
//
////		synchronized (TitlePartyService.class) {
//			  long t1 = System.currentTimeMillis();
//			int count = 0;
//			while (count <1) {
//				 
//			   try {
//
//				   List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//
//				   params.add(new BasicNameValuePair("title",news.getT()));
//			
//				   params.add(new BasicNameValuePair("body",news.getCo()));
//	
//			
////				   params.add(new BasicNameValuePair("type","taginner"));
//				 
//
//				   httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
////				   LOG.info("g2 post===>>"+new UrlEncodedFormEntity(params,HTTP.UTF_8));
//				   HttpResponse response;
//				
//				   response=httpclient.execute(httppost);
//				  
////				   if(response==null){
////					   return news;
////				   }
//				  HttpEntity entity = response.getEntity();
//				  byte[] pageData = EntityUtils.toByteArray(entity);
//				  result = new String(pageData,"utf-8");
//				  long t2 = System.currentTimeMillis();
//				 
//				  LOG.info("TitlePartyService return==nid is= "+news.getNid()+" return t = "+(t2-t1));
////				  System.out.println("returndata===>>"+result);
//				  break;
//				  } catch (Exception e) {
//
//					   // TODO Auto-generated catch block
//					  e.printStackTrace();
//					  LOG.error("TitlePartyService request error ==", e);
////						try {
////							Thread.sleep(200);
////						} catch (InterruptedException e1) {
////							// TODO Auto-generated catch block
////							e1.printStackTrace();
////						}
////						LOG.info("[F-C-01] TitlePartyService exception, reconnectiong for "
////								+ count + " times...");
//					
////						httppost.abort();
//					
////						httpclient = new DefaultHttpClient();				
//
//				  } 
//			   count++;
//			}
//
//
//			if (result.equals("-1")) {
//				LOG.error("[F-C-01] TitlePartyService return null, oid:" + news.getOid() + " nid:" + news.getNid() );
//				return;
//			} else {
//				try {
//					JSONObject receive = JSONObject.fromObject(result);
//					String res = receive.getString("data").trim();
//
//
//					JSONArray guidShopList_obj = JSONArray.fromObject(res) ;
//
//					if (guidShopList_obj == null || guidShopList_obj.size() == 0) {
//						LOG.error("[F-C-01] TitlePartyService result error: "
//								+ result
//								+ " nid="
//								+ news.getNid()
//								+ " oid="
//								+ news.getOid() + " from=" + news.getFrom());
//						return;
//					}
//
//				        	JSONObject TP = (JSONObject) guidShopList_obj.get(0);
////				        	System.out.println("key 2 = "+key2);
//				        	int type = TP.getInt("type");
//				        	Double confidence = TP.getDouble("confidence");
////				        news.setType(type);
////				        news.setConfidence(confidence);
//				        long t3 = System.currentTimeMillis();
//				
//				LOG.info(" news TitlePartyService set over  nid = "+news.getNid()+" type = "+news.getType()+" con = "+news.getConfidence()+" title = "+news.getT()+" all use t = "+(t3-t1));
//					return;
//				} catch (Exception e) {
//					LOG.error("Exception during tackle TitlePartyService result",e);
//					e.printStackTrace();
//					return;
//				}
//			}
//		}
////	}
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		long t1 =System.currentTimeMillis();
//		int n =1;
//		for(int i = 0;i<n;i++){
//		MrdNewsObject newsObj = new MrdNewsObject();
//		newsObj.setT("毛里求斯旅游攻略及避免上当指南 篇二：北京—迪拜的半日行程");
//		newsObj.setCo("券商研报普遍预测,国企改革的投资机会主要集中在以下五个板块,分别是航运板块、有色金属板块、建筑建材、汽车和钢铁行业。继南北车整合完成后,上周五中远中海等央企集中停牌引发市场央企合并整合加速的遐想,国企特别是央企改革受益标的或迎来投资的;风口受此影响,央企改革概念股今日大涨,多家主流券商研究机构不约而同发布研报认为国企改革近期或有大动作,相关股票是下半年重点把握的投资方向。券商研报普遍预测,国企改革的投资机会主要集中在以下五个板块,分别是航运板块、有色金属板块、建筑建材、汽车和钢铁行业周五晚上,中海集运、中海发展、中国远洋、中远航运、中海科技等5家公司同时公告宣布停牌,原因都是接控股股东通知,其正在筹划重大事项。对此,国金证券指出,当前航运系集体停牌,该事件将引爆同样具备强烈整合预期的船舶系,在国企改革顶层设计方案纲领性文件出台时间亦不会太久的大背景下,建议投资者积极关注船舶系,对应的受益标的为:中国重工、中船防务、中国船舶、钢构工程。安信证券则认为,除上周五停牌的5家企业外,中国外运长航集团有限公司旗下的外运发展和招商局轮船股份有限公司旗下的上市公司招商轮船也都有合并的预期。 不过,国泰君安认为,航运板块首选中海海盛。&ldquo;原因是公司已公告将转型为民营非航运股,且已澄清不参与此次重组,因此在其他公司停牌期间成为稀缺标的,我们近期行业调研后看好公司转型决心;其次利好招商轮船,因为不参与两大集团重组所以无需停牌 航运板块央企改革主要标的:中海集运、中海发展、中国远洋、中远航运、中海科技、中国重工、中船防务、中国船舶、钢构工程、招商轮船。中国有色金属工业协会近日组织召开《有色金属工业&ldquo;十三五&rdquo;规划研究》启动会,部署开展有色金属行业&ldquo;十三五&rdquo;规划研究工作。有色金属也被认为是央企改革的一个重要标的。国泰君安指出,有色国企合并加速,利好有色金属行业。首先有利于加快去杠杆和产能出清,其次是加强国内企业海外资源获取和定价能力,最后是减少海外拿项目订单的恶性竞争,国泰君安重点推荐中色股份。招商证券指出,&ldquo;大国战略&rdquo;下,有色系统央企改革势在必行。有色央企整合有两个层次:有色央企集团之间的整合,或者集团内部的整合。央企层面,中国有色矿业集团是最优整合标的,强烈推荐中色股份:如果是集团内部整合,公司无疑是整合平台(中国有色矿业集团层面还有优质资产);如果是央企之间的整合,公司无疑是其他央企的必争之地。公司同时也是&ldquo;一带一路&rdquo;真正的最先受益者之一。招商证券认为,公司具备在未来两年加速开发海内外有色金属资源的实力,全球范围类抗衡能力的&ldquo;综合类矿业公司&rdquo;雏形呼之欲出。其它央企改革标的还可关注中国中冶、株冶集团。有色板块央企改革主要标的:中色股份、中国中冶、株冶集团。");
//		newsObj.setNid("123456789");
//		newsObj.setMe("今日财富投资");
//		newsObj.setSub_cl("20200");
//		setNewsTitleParty(newsObj);
//		}
//		long t2 = System.currentTimeMillis();
//		System.out.println(" use time = "+(t2-t1)/n);
//	}
//
//}
