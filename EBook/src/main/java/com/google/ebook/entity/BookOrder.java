package com.google.ebook.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="orders")
public class BookOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name="order_id")
	private int orderId;
	private String name;
	private String email;
	private String mobile;
	
	@OneToOne
	private Address address;
	
	private String payment;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private String date;
	
	@OneToMany(mappedBy = "order")
	private List<BookDetails> books;
	
	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public List<BookDetails> getBooks() {
		return books;
	}


	public void setBooks(List<BookDetails> books) {
		this.books = books;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	public BookOrder() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "BookOrder [ orderId=" + orderId + ", name=" + name + ", email=" + email + ", mobile="
				+ mobile + ", payment=" + payment + ", date=" + date + "]";
	}
	
	
}
