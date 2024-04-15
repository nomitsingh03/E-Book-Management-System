package com.google.ebook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ADDRESS")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int add_Id;
	private String locality;
	private String landmark;
	private String city;
	private String state;
	private String district;
	private String pincode;
	private boolean def;
	
	@OneToOne(mappedBy = "address")
	private BookOrder order;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public boolean isDef() {
		return def;
	}
	public void setDef(boolean def) {
		this.def = def;
	}
	public BookOrder getOrder() {
		return order;
	}
	public void setOrder(BookOrder order) {
		this.order = order;
	}
	public int getAdd_Id() {
		return add_Id;
	}
	public void setAdd_Id(int add_Id) {
		this.add_Id = add_Id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String address) {
		this.locality = address;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	@Override
	public String toString() {
		return locality + ", city-" +city+ ", dist-" +district + ", state-"+ state
				+ "," + pincode;
	}
	
	
}
