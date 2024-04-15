package com.google.ebook.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.BookOrder;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.repository.BookRepository;
import com.google.ebook.repository.OrderRepository;
import com.google.ebook.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Value("${project.image}")
	private String path;
	
	private User user;
	
	@Autowired
	private CartHelper cartHelper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	
	@GetMapping
	public String home() {
		return "admin/home";
	}
	
	@ModelAttribute("user")
	public void common(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
			String username = user.getAttribute("email");
			
			model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
			this.user=userRepo.findByEmail(username);
			model.addAttribute("user",user );
		}else {
			if(p!=null) {
				this.user = userRepo.findByEmail(p.getName());
				model.addAttribute("user", user);
			}
		}
		model.addAttribute("cartQuantity", cartHelper.common(user));
		System.out.println(p);
		
	}
	
	@GetMapping("/addBook")
	public String book(Model m) {
		m.addAttribute("book", new BookDetails());
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/add_book";
	}
	
	@GetMapping("/order-history")
	public String orderDetails(Model model) {
		List<BookOrder> orders = orderRepo.findAll();
		model.addAttribute("orders",orders);
		model.addAttribute("user", user);
		model.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/order";
	}
	
}
