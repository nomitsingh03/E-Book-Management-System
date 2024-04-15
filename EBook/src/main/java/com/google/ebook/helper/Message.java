package com.google.ebook.helper;

public class Message {

	private String content;
	private String type;
	private String icon;
	
	
	public Message(String content, String type, String icon) {
		super();
		this.content = content;
		this.type = type;
		this.icon=icon;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getType() {
		return type;
	}

	

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
