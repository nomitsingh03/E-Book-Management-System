package com.google.ebook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {

	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private BookDetails book;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private int quantity;
	
	@Column(name = "total_price")
	private Double totalPrice;

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public BookDetails getBook() {
		return book;
	}

	public void setBook(BookDetails book_id) {
		this.book = book_id;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	
}
