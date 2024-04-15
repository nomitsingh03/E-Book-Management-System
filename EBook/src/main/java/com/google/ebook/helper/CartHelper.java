package com.google.ebook.helper;

import org.springframework.stereotype.Component;

import com.google.ebook.entity.User;


@Component
public class CartHelper {

	public int common(User user) {
		return user.getCartItems().size();
}
}
