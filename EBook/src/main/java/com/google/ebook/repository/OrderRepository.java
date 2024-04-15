package com.google.ebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.google.ebook.entity.BookOrder;
import com.google.ebook.entity.User;

public interface OrderRepository extends JpaRepository<BookOrder, Integer>{

	List<BookOrder> findByUser(User user);
}
