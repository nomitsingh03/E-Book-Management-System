package com.google.ebook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.ebook.entity.BookOrder;
import com.google.ebook.repository.OrderRepository;



@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	
	public List<BookOrder> getAllOrders() {
		return this.orderRepository.findAll();
	}
	
}