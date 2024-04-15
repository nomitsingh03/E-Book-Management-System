package com.google.ebook.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.ebook.entity.Address;
import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.BookOrder;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.repository.AddressRepository;
import com.google.ebook.repository.CartRepository;
import com.google.ebook.repository.OrderRepository;
import com.google.ebook.repository.UserRepository;

@Controller
@RequestMapping("/order")
public class OrderController {

	private User user;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private CartHelper cartHelper;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@ModelAttribute("user")
	public void display(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
			String username = user.getAttribute("email");

			model.addAttribute("userDetails",
					user.getAttribute("name") != null ? user.getAttribute("name") : user.getAttribute("login"));
			this.user = userRepo.findByEmail(username);
			
		} else {
			if (p != null) {
				this.user = userRepo.findByEmail(p.getName());
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("cartQuantity", cartHelper.common(user));
		System.out.println(p);
	}
	
	@PostMapping("/order-processed")
	public String order(@ModelAttribute("address") Address address,@RequestParam("name")String name,
			@RequestParam("email")String email, @RequestParam("mobile")String mobile, @RequestParam("payment")String payment,
			Model model ) {
		BookOrder order = new BookOrder();
		List<Cart> items = user.getCartItems();
		if(items.isEmpty()) return "redirect:/";
		List<BookDetails> books = new ArrayList<>();
		for(Cart item : items) {
			books.add(item.getBook());
			item.setUser(null);
			item.setBook(null);
			cartRepo.deleteById(item.getCartId());
		}
		address.setUser(user);
		if(name.equals("")) name=user.getName(); 
		order.setName(name);
		if(email.equals("")) email=user.getEmail();
		order.setEmail(email);
		order.setMobile(mobile);
		order.setAddress(addressRepo.save(address));
		order.setBooks(books);
		order.setUser(user);
		Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = sdf.format(now);
		order.setDate(formattedDateTime);
		order.setPayment(payment);
		orderRepo.save(order);
		
		model.addAttribute("user", user);
		model.addAttribute("cartQuantity", cartHelper.common(user));
		return "user/order-success";
	}
}
