package com.bezkoder.spring.files.upload.db.model;

public class StorageServiceRequest{
    public String image_data;
    public String imageName;
    public String bucketName;
    public String serviceType;
    public String extension;
    
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getImage_data() {
		return image_data;
	}
	public void setImage_data(String image_data) {
		this.image_data = image_data;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
