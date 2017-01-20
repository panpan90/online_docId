package com.sohu.mrd.videoDocId.model;
/**
 * @author Jin Guopan
   @creation 2017年1月17日
 */
public class NewSimilaryInfo {
	private Double titleJaccard;
	private Integer hanmingDistance;
	private String docId;
	private String url;
	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the docId
	 */
	public String getDocId() {
		return docId;
	}
	/**
	 * @param docId the docId to set
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}
	/**
	 * @return the titleJaccard
	 */
	public Double getTitleJaccard() {
		return titleJaccard;
	}
	/**
	 * @param titleJaccard the titleJaccard to set
	 */
	public void setTitleJaccard(Double titleJaccard) {
		this.titleJaccard = titleJaccard;
	}
	/**
	 * @return the hanmingDistance
	 */
	public Integer getHanmingDistance() {
		return hanmingDistance;
	}
	/**
	 * @param hanmingDistance the hanmingDistance to set
	 */
	public void setHanmingDistance(Integer hanmingDistance) {
		this.hanmingDistance = hanmingDistance;
	}
	
}
