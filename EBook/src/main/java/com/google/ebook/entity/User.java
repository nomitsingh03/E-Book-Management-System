package com.google.ebook.entity;


import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(unique = true)
	private String email;
		
	@Column(name = "mobile_number")
	private String mobileNo;
	
	private String password;
	private String role;
	private boolean enable; 
	private String verificationCode;
	private boolean isAccountNonLocked;
	private int failedAttempt;
	private Date lockTime;
	
	@OneToMany(mappedBy = "user")
	private List<Cart> cartItems;
	
	@OneToMany(mappedBy = "user", orphanRemoval =true)
	private List<BookOrder> books;
	
	@OneToMany(cascade =CascadeType.ALL, fetch=FetchType.LAZY, mappedBy ="user" )
	private List<Address> perAddress;
	
	@OneToMany(mappedBy = "user", cascade =CascadeType.ALL, fetch=FetchType.LAZY)
	private List<BookDetails> bookDetails;

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
//	private List<Address> contacts = new ArrayList<>();
	
	
	
	public List<Address> getAddress() {
		return perAddress;
	}

	public void setAddress(List<Address> address) {
		this.perAddress = address;
	}

	public int getId() {
		return user_id;
	}

	public void setId(int id) {
		this.user_id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public List<Cart> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<Cart> cartItems) {
		this.cartItems = cartItems;
	}

	public List<BookOrder> getBooks() {
		return books;
	}

	public void setBooks(List<BookOrder> books) {
		this.books = books;
	}

	public List<BookDetails> getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(List<BookDetails> bookDetails) {
		this.bookDetails = bookDetails;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public int getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(int failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	@Override
	public String toString() {
		return "User [id=" +user_id + ", name=" + name + ", email=" + email + ", mobileNo=" + mobileNo + ", password="
				+ password + ", role=" + role + ", enable=" + enable
				+ ", verificationCode=" + verificationCode + ", isAccountNonLocked=" + isAccountNonLocked
				+ ", failedAttempt=" + failedAttempt + ", lockTime=" + lockTime + "]";
	}

	

}

