package com.google.ebook.controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.ebook.entity.User;
import com.google.ebook.helper.OtpSender;
import com.google.ebook.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {
	
	Random random = new Random(1000);
	
	@Autowired
	private OtpSender otpSender;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	User user; 
	
	@ModelAttribute("user")
	public void display(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
		DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
		String username = user.getAttribute("email");
		
		model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
		this.user=userRepo.findByEmail(username);
		model.addAttribute("user",user);
		}else 
			if(p!=null) {
			this.user = userRepo.findByEmail(p.getName());		
			model.addAttribute("user",user);
			
		} else
			model.addAttribute("user",new User());
		System.out.println(p);
	}

	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "normal/forgot_password";
	}
	
	@PostMapping("/get-otp")
	public String forgot(@Param("email") String email, HttpSession session) {
		User requestUser = userRepo.findByEmail(email);
		try {
			if(requestUser==null) {
				throw new Exception("Your email is not registered");
			}
			int otp = random.nextInt(999999);
			System.out.println(otp);
			session.setAttribute("msg", "OTP sent successfully, Check Your Email");
			session.setAttribute("otp", String.valueOf(otp));
			session.setAttribute("email", email);
			otpSender.sendEmail(requestUser, otp);
			return "normal/otp_validation";
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("msg", "Something wrong..."+e.getMessage());
			return "normal/forgot_password";
		}
	}
	
	@PostMapping("/otp-validation")
	public String validateOtp(@Param("email") String email, @Param("otp") String otp , HttpSession session, Model model) {
		String myOtp = (String)session.getAttribute("otp");
		if(Integer.parseInt(otp)!=Integer.parseInt(myOtp)) {
			session.setAttribute("msg","You entered wrong OTP...");
			return "redirect:/forgot-password";
		} else {
			session.setAttribute("email", email);
			return "normal/change_password";
		}
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@Param("password1") String password1, @Param("password2") String password2,
				HttpSession session,Model model) {
		if(!password1.equals(password2)) {
			session.setAttribute("failmsg", "Password and Confirm Password are different");
			return "normal/change_password";
		} else {
			User existuser =userRepo.findByEmail((String)session.getAttribute("email"));
			System.out.println(existuser);
			existuser.setPassword(passwordEncoder.encode(password2));
			userRepo.save(existuser);
			session.setAttribute("msg", "Your Password is reset, Enter new Password to login");
			return "redirect:/forgot-password";
		}
	}
}
