package com.google.ebook.controller;

import java.security.Principal;

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

import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.BookService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CartHelper cartHelper;
	
	private User user;
	
	@Autowired
	private BookService bookService;
	
	@ModelAttribute
	public void display(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
		DefaultOAuth2User defaultUser = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
		String username = defaultUser.getAttribute("email");
		
		model.addAttribute("userDetails", defaultUser.getAttribute("name")!= null ?defaultUser.getAttribute("name"):defaultUser.getAttribute("login"));
		this.user=userRepo.findByEmail(username);
		System.out.print(user);
		model.addAttribute("user",user);
		}else 
			if(p!=null) {
			this.user = userRepo.findByEmail(p.getName());		
			model.addAttribute("user",user);
			
		} else
			model.addAttribute("user",new User());
//		System.out.println(p);
	}
	
	@GetMapping
	public String home(Model model) {
		System.out.println(user);
		model.addAttribute("recentbooks", bookService.getRecentBooks());
		model.addAttribute("oldbooks", bookService.getOldBooks());
		model.addAttribute("newbooks", bookService.getNewBooks());
		model.addAttribute("programming", bookService.getProgrammingBooks());
		model.addAttribute("communication", bookService.getCommunicationBooks());
		model.addAttribute("computer", bookService.getComputerBooks());
		if(user!=null)
		model.addAttribute("cartQuantity", cartHelper.common(user));
	return "normal/home";
	}
	
	@GetMapping("/setting")
	public String setting(Model model) {
		System.out.println(user);
		if(user==null)
		return "redirect:/login";
		else model.addAttribute("user", user);
		return "user/setting";
	}
	
	@GetMapping("/help-center")
	public String help(Model model) {
		if(user==null)
			return "redirect:/login";
			else model.addAttribute("user", user);
			return "normal/helpline";
	}
	
}
