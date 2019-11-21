package com.android.domain;

public class courseDoc {
	String courseid,ArticleID, MainHead, ArticleTypeName;

	public courseDoc() {
	}

	public courseDoc(String cid, String ArticleID, String MainHead, String ArticleTypeName) {
		this.courseid = cid;
		this.ArticleID = ArticleID;
		this.MainHead = MainHead;
		this.ArticleTypeName = ArticleTypeName;
	}

	public void setcourseid(String cid){
		this.courseid = cid;
	}
	public String getcourseid(){
		return courseid;
	}
	public void setArticleID(String ArticleID) {
		this.ArticleID = ArticleID;
	}

	public String getArticleID() {
		return ArticleID;
	}

	public void setMainHead(String MainHead) {
		this.MainHead = MainHead;
	}

	public String getMainHead() {
		return MainHead;
	}

	public void setArticleTypeName(String ArticleTypeName) {
		this.ArticleTypeName = ArticleTypeName;
	}

	public String getArticleTypeName() {
		return ArticleTypeName;
	}

	@Override
	public String toString() {
		return "courseDoc [courseid=" + courseid + ", ArticleID=" + ArticleID + ", ArticleTypeName=" + ArticleTypeName + ",MainHead=" + MainHead + "]";
	}
}
