package com.google.ebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.ebook.entity.User;
import com.google.ebook.helper.Message;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/registration")
public class Registration {
	
	@Autowired
	public UserService userService;
	
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@ModelAttribute("user")
	public User user() {
		return new User();
	}
	
	@GetMapping
	public String showRegistration(Model m) {
		m.addAttribute("user",new User());
		return "normal/register";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") User user, HttpSession session, Model model ,HttpServletRequest request) {
		if(userService.userExist(user)) {
			session.setAttribute("msg","Email already registered");
			return "redirect:/registration";
		} 
		String url = request.getRequestURL().toString();
		url=url.replace(request.getServletPath(),"");
		User newUser =userService.saveUser(user, url);
		if(newUser!=null) { 
			session.setAttribute("msg","Registration Successfully");
		  } else {
			  session.setAttribute("msg", "Something error...."); }
		return "redirect:/login";
		
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model m) {
		boolean flag = userService.verifyAccount(code);
		if(flag) {
			m.addAttribute("msg",new Message("Successfully Your Account is Verified","text-success", "fa-circle-check"));
		} else { 
			m.addAttribute("msg",new Message("Verification failed","text-danger","fa-circle-xmark"));
		}
		return "normal/message";
	}
}
