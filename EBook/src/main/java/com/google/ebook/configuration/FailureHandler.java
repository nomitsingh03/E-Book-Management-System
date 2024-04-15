package com.google.ebook.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.google.ebook.entity.User;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String email = request.getParameter("username");
		User user = userRepo.findByEmail(email);
		
		if(user!=null) 
		{
			if(user.isEnable()) {
				if(user.isAccountNonLocked())
				{
					if(user.getFailedAttempt()< UserService.ATTEMPT_TIME-1) {
						userService.increaseFailedAttempt(user);
					} else {
						userService.lock(user);
						exception = new LockedException("You have one attempt left !! failed Attempt 3 times , try after 60 seconds");
					}
				} else if(!user.isAccountNonLocked()) 
				{
						if(userService.unLockAccountTimeExpired(user))
						exception = new LockedException("Account is unlocked! Please try to Login with correct Credentials");
					
				}
			}else {
				exception = new LockedException("Account is inactive. Verify account ");
			}
		}
		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}

