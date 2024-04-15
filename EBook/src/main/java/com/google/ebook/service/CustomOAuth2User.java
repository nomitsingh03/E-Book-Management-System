package com.google.ebook.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
	
	
	private OAuth2User oAuth2User;
	

	public CustomOAuth2User(OAuth2User oAuth2User) {
		super();
		this.oAuth2User = oAuth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		Map<String, Object> map =oAuth2User.getAttributes();
		map.put("role", "ROLE_USER");
		return map;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttribute("name");
	}
	
	public String getEmail() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttribute("email");
	}
	
	
	
}
