package com.google.ebook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;

public interface CartRepository extends JpaRepository<Cart, Integer>{

	Cart findByUserAndBook(User user, BookDetails book);
	
	List<Cart> findByUser(User user);
	
}
