package com.google.ebook.configuration;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.ebook.entity.User;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
			OAuth2User userDetails = (OAuth2User) authentication.getPrincipal();
			Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			String username = userDetails.getAttribute("email");
			if(userRepository.findByEmail(username)==null) {
				User user = new User();
				user.setEmail(username);
				user.setName(userDetails.getAttribute("name"));
				user.setPassword("");
				user.setRole("ROLE_USER");
				user.setAccountNonLocked(true);
				user.setEnable(true);
				userRepository.save(user);
				
			}
		}

			new DefaultRedirectStrategy().sendRedirect(request, response, "/");

	}

}
