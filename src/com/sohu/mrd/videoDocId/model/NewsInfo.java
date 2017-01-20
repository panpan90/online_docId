package com.sohu.mrd.videoDocId.model;
/**
 * @author Jin Guopan
   @creation 2017年1月17日
 */
public class NewsInfo {
	private String url;
	private String content;
	private String  title;
	private String category;
	private boolean isGroupImage;
	private String   introduce; //组图类型的简介文字
	/**
	 * @return the isGroupImage
	 */
	public boolean isGroupImage() {
		return isGroupImage;
	}
	/**
	 * @param isGroupImage the isGroupImage to set
	 */
	public void setGroupImage(boolean isGroupImage) {
		this.isGroupImage = isGroupImage;
	}
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the introduce
	 */
	public String getIntroduce() {
		return introduce;
	}
	/**
	 * @param introduce the introduce to set
	 */
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
}
