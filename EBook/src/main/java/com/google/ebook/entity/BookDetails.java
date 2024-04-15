package com.google.ebook.entity;

import java.util.List;

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

@Entity
public class BookDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private int book_id;
	
	@Column(name="book_name")
	private String bookName;
	
	@Column(name = "book_price")
	private Double bookPrice;
	
	private String author;
	
	@Column(name = "book_category") 
	private String bookCategory;
	
	@Column(name = "book_status")
	private String bookStatus;
	
	private String description;
	
	@OneToMany(mappedBy = "book")
	private List<Cart> cart;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public BookOrder getOrder() {
		return order;
	}

	public void setOrder(BookOrder order) {
		this.order = order;
	}

	@Column(name="book_img")
	private String bookImageName;

	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "book_type")
	private String bookType; 
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private BookOrder order;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public BookDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BookDetails(String book, Double bookPrice, String author, String bookCategory, String bookStatus, String bookType,
			String bookImageName, String userEmail) {
		super();
		this.bookName = book;
		this.bookPrice = bookPrice;
		this.author = author;
		this.bookCategory = bookCategory;
		this.bookStatus = bookStatus;
		this.bookType=bookType;
		this.bookImageName = bookImageName;
		this.userEmail = userEmail;
	}
	
	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String book) {
		this.bookName = book;
	}

	public Double getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(Double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(String bookCategory) {
		this.bookCategory = bookCategory;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getBookImageName() {
		return bookImageName;
	}

	public void setBookImageName(String bookImageName) {
		this.bookImageName = bookImageName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "BookDetails [bookId=" + book_id + ", bookName=" + bookName + ", bookPrice=" + bookPrice + ", author=" + author
				+ ", bookCategory=" + bookCategory + ", bookStatus=" + bookStatus + ", bookImageName=" + bookImageName
				+ ", userEmail=" + userEmail + "]";
	}
	
	
	
}
