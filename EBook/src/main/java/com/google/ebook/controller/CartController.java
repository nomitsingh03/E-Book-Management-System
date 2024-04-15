package com.google.ebook.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.ebook.entity.Address;
import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.helper.Message;
import com.google.ebook.helper.UserAuthorization;
import com.google.ebook.repository.BookRepository;
import com.google.ebook.repository.CartRepository;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private UserAuthorization userAuth;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartHelper cartHelper;
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	private User user;
	
	public Address address;
	
	@ModelAttribute("user")
	public void display(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
			String username = user.getAttribute("email");

			model.addAttribute("userDetails",
					user.getAttribute("name") != null ? user.getAttribute("name") : user.getAttribute("login"));
			this.user = userRepo.findByEmail(username);
			model.addAttribute("user", user);
		} else {
			if (p != null) {
				this.user = userRepo.findByEmail(p.getName());
				model.addAttribute("user", user);
			}
		}
		model.addAttribute("cartQuantity", cartHelper.common(user));
		System.out.println(p);
	}
	
	
	@GetMapping("/")
	public String cart(Model model,HttpSession session ) {
		
		List<Cart> cartItems = cartRepo.findByUser(user);
		if(cartItems.isEmpty()) {
			model.addAttribute("carts", cartItems);
			model.addAttribute("user",user);
			if(address==null)
				model.addAttribute("address",new Address());
				else 
					model.addAttribute("address", address);
		session.setAttribute("message", new Message("No book added to Cart", "", "fa-circle-exclamation"));
		} else {
			double totalPrice=0;
			for(Cart cart : cartItems) {
				totalPrice +=cart.getTotalPrice();
			}
		model.addAttribute("totalCartPrice", totalPrice);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("user",user);
		model.addAttribute("cartQuantity", cartHelper.common(user));
		Address address=userService.getAddress(user);
		if(address==null)
		model.addAttribute("address",new Address());
		else 
			model.addAttribute("address", address);
		}
		return "user/add_cart";
	}

	@GetMapping("/add-item/{id}")
	public String item(@PathVariable("id") int bookId, HttpSession session, RedirectAttributes redirectAttr) {
		System.out.println(user);
		if(user==null) return "redirect:/login";
		BookDetails book = bookRepo.findById(bookId).get();
		if(book!=null)
		{
			System.out.println(101);
			Cart oldCart = cartRepo.findByUserAndBook(user,book);
			System.out.println(oldCart);
			if(oldCart==null) {
				System.out.println(105);
			Cart cart = new Cart();
			cart.setBook(book);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(book.getBookPrice());
			cartRepo.save(cart);
		} else{
			System.out.println(102);
			oldCart.setQuantity(oldCart.getQuantity()+1);
			oldCart.setTotalPrice(oldCart.getQuantity()*book.getBookPrice());
			cartRepo.save(oldCart);
		}
			System.out.println(103);
			redirectAttr.addFlashAttribute("success", "Book Added to Cart");
		}
		else {
			System.out.println(104);
			redirectAttr.addFlashAttribute("error", "Book not added to cart!");
			redirectAttr.addFlashAttribute("cartQuantity", cartHelper.common(user));
		}
		return "redirect:/user/all_newBooks";
	}
	
	@GetMapping("/remove-item/{id}")
	public String update(@PathVariable("id") int cartId) {
		System.out.println(user);
		if(user==null) return "redirect:/login";
		cartRepo.deleteById(cartId);
		return "redirect:/cart/";
	}
	
	
	
}
