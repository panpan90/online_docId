///**
// * Project Name:NewsPreprocessor_v1.0_b
// * File Name:DocIDService.java
// * Package Name:com.sohu.mrd.engine.preprocess
// * Date:2014年5月29日下午3:52:37
// * Copyright (c) 2014, alexma@sohu-inc.com All Rights Reserved.
// *
// */
///**
// * Project Name:NewsPreprocessor_v1.0_b
// * File Name:DocIDService.java
// * Package Name:com.sohu.mrd.engine.preprocess
// * Date:2014年5月29日下午3:52:37
// * Copyright (c) 2014, alexma@sohu-inc.com All Rights Reserved.
// *
// */
//
//package com.sohu.mrd.preprocess.common.service;
//
//import java.util.List;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//import org.apache.thrift.transport.TTransportException;
//
//import com.sohu.mrd.preprocess.common.object.MrdNewsObject;
//import com.sohu.mrd.preprocess.common.object.MrdNewsObject.NewsKeyWordResult;
//import com.sohu.mrd.preprocess.common.thrift.NewsChannel;
//
//
//public class KeywordService {
//	// Log handler
//	private static final Log LOG = LogFactory.getLog(KeywordService.class);
//
//	private static TTransport transport = null; // thrift transport
//	private static TProtocol protocol = null; // thrift protocol
//	private static NewsChannel.Client client = null; // NewsChannel client
//
//	// initialize news server thrift client
//	static {
//		transport = new TSocket("10.13.82.20", 7911,1000); // new cate classify online
//		protocol = new TBinaryProtocol(transport);
//		try {
//		
//			transport.open();
//			
//			client = new NewsChannel.Client(protocol);
//		} catch (TTransportException e) {
//			// TODO Auto-generated catch block
//			LOG.error("Exception during thrift init on NewsChannel");
//			e.printStackTrace();
//		}
//	}
//	
//	public static void setNewsKeyword(MrdNewsObject news) throws Exception{
//		if(news == null){
//			LOG.error("[F-C-01] Error in null news channel object");
//			return;
//		}
//		
//		String request = news.getT() + "\001" + news.getMe() + "\001" + news.getCo();
//		String result = "-1";
//		
//		synchronized (KeywordService.class) {
//
//			int count = 1;
//			while (count <= 5) {
//				try {
//					result = client.getKeywords(request, 10);
//					break;
//				} catch (Exception e) {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					LOG.error("[F-C-01] Thrift service(getNewsKeyword) exception, reconnectiong for "
//							+ count + " times...");
//					transport.close();
//					try {
//						transport.open();
//					} catch (TTransportException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					client = new NewsChannel.Client(protocol);
//				}
//				count++;
//			}
//			
//			if(result != null && result.length() > 0){
//				String[] pairs = result.split(",");
//				NewsKeyWordResult[] kw_list = new NewsKeyWordResult[pairs.length];
//				for(int i = 0; i < pairs.length; ++i){
//					String[] ss = pairs[i].split(":");
//					NewsKeyWordResult kr = new NewsKeyWordResult(ss[0], Double.parseDouble(ss[1]) / 10.0);
//					kw_list[i] = kr;
//				}
//				
//				news.setKwold(kw_list);
//				LOG.info("use 7911 set old kw  = "+news.getNid()+" kw length = "+news.getKwold().length);
//				if(news.getKw().length==0){
//					news.setKw(kw_list);
//					LOG.info("use 7911 set kw  = "+news.getNid()+" kw length = "+news.getKw().length);
//				}
//			}else{
//				throw new Exception("Keyword result is null!");
//			}
//		}
//	}
//
//
//	/**
//	 * main:
//	 * 
//	 * @author alexma
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
////		setNewsKeyword();
//	}
//
//}
