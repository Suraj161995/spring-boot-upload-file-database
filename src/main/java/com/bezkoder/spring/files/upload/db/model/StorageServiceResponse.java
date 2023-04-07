package com.bezkoder.spring.files.upload.db.model;

public class StorageServiceResponse{
    public int code;
	public String message;
    public String url;
    
    public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
