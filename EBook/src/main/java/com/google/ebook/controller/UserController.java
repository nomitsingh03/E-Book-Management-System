package com.google.ebook.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.google.ebook.entity.Address;
import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.BookOrder;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.helper.Message;
import com.google.ebook.repository.AddressRepository;
import com.google.ebook.repository.BookRepository;
import com.google.ebook.repository.OrderRepository;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	@Autowired
	private CartHelper cartHelper;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@ModelAttribute("user")
	public void display(Model model, Principal p) {
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
	
	@GetMapping
	public String home() {
		return "user/profile";
	}
	
	@GetMapping("/view_book/{id}")
	public String viewBook(@PathVariable("id") int id,Model m) {
		BookDetails book = bookRepo.findById(id).get();
		m.addAttribute("book", book);
		m.addAttribute("seller", book.getUser());
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "user/book_view";
	}
	
	@GetMapping("/edit-profile/{user}")
	public String updateProfile(Model m) {
		m.addAttribute("user",user);
		
		Address address=userService.getAddress(user);
		if(address==null)
		m.addAttribute("address",new Address());
		else 
			m.addAttribute("address", address);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/edit_profile";
	}
	
	@PostMapping("/update-profile")
	public String updateProfileProcess(@ModelAttribute("user")User user, HttpSession session) {
		this.user = userRepo.save(user);
		session.setAttribute("msg", new Message("Profile Successfully Updated!!", "alert-success","fa-circle-check"));
		return "redirect:/user/edit-profile/"+user.getName();
	}
	@PostMapping("/update-address")
	public String updateAddressProcess(@ModelAttribute("address") Address address,HttpSession session) {
		address.setUser(user);
		Address savedAdd =  addressRepo.save(address);
		session.setAttribute("msg", new Message("Address Successfully Updated!!", "alert-success","fa-circle-check"));
		return "redirect:/user/edit-profile/"+user.getName();
	}
	
	@GetMapping("/order-history")
	public String orderDetails(Model model) {
		List<BookOrder> orders = orderRepo.findByUser(user);
		model.addAttribute("orders",orders);
		model.addAttribute("user", user);
		model.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/order";
	}
	
}
