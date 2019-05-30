package com.example.uas.models;

public class Universitas {
	private String nama;
	private String alamat;
	private String kabupaten;
	private String website;
	private Double latitute;
	private Double longitude;
	private String urlLogo;
	private String key;
	
	public Universitas() {
	}
	
	public Universitas(String nama, String alamat, String kabupaten, String website, Double latitute, Double longitude, String urlLogo) {
		this.nama = nama;
		this.alamat = alamat;
		this.kabupaten = kabupaten;
		this.website = website;
		this.latitute = latitute;
		this.longitude = longitude;
		this.urlLogo = urlLogo;
	}
	
	public String getNama() {
		return nama;
	}
	
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public String getAlamat() {
		return alamat;
	}
	
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	public String getKabupaten() {
		return kabupaten;
	}
	
	public void setKabupaten(String kabupaten) {
		this.kabupaten = kabupaten;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public Double getLatitute() {
		return latitute;
	}
	
	public void setLatitute(Double latitute) {
		this.latitute = latitute;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getUrlLogo() {
		return urlLogo;
	}
	
	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
}
