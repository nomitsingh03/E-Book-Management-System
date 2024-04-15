package com.google.ebook.helper;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.ebook.entity.User;
import com.google.ebook.repository.UserRepository;

@Component
public class UserAuthorization {

	@Autowired
	private UserRepository userRepo;

	private User user;

	
}
