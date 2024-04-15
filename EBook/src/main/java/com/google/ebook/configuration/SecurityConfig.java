package com.google.ebook.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import com.google.ebook.service.CustomOAuth2DefaultService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	public CustomAuthSuccessHandler successHandler1;
	
	@Autowired
	public CustomOauth2SuccessHandler successHandler2;
	
	@Autowired
	public FailureHandler failureHandler;
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService getDetailsService() {
		
		return new CustomUserDetailsService();
	}
	
	public DefaultOAuth2UserService oAuth2UserService() {
		return new CustomOAuth2DefaultService();
	}
	
	@Bean
	public DaoAuthenticationProvider getAuthenticetionProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(scrf->scrf.disable())
		.authorizeHttpRequests(authorizeHttpRequest->authorizeHttpRequest
				.requestMatchers("/registration/**","/login/**","/webjars/**").permitAll()
//				.requestMatchers("/user/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
				.requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
//				.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated())
				.formLogin(login ->login.loginPage("/login").loginProcessingUrl("/userLogin").failureHandler(failureHandler).successHandler(successHandler1))
				.oauth2Login(oauth2->oauth2
//						.loginProcessingUrl("/login")
						.loginPage("/login").successHandler(successHandler2)
						.userInfoEndpoint(info->info
								.userService(this.oAuth2UserService())
								.oidcUserService(this.oidcUserService()))
//						.successHandler(successHandler2)
						);
		
				http.logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/login"));
				;
		
		return http.build();
	}
	
	private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		final OidcUserService delegate = new OidcUserService();

		return (userRequest) -> {
			// Delegate to the default implementation for loading a user
			OidcUser oidcUser = delegate.loadUser(userRequest);

			OAuth2AccessToken accessToken = userRequest.getAccessToken();
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			for (String authority : accessToken.getScopes()) {
				mappedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
			}
			mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());

			return oidcUser;
		};
	}
	
	
//	private GrantedAuthoritiesMapper userAuthoritiesMapper() {
//		return (authorities) -> {
//			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//			authorities.forEach(authority -> {
//				if (OidcUserAuthority.class.isInstance(authority)) {
//					OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;
//
//					OidcIdToken idToken = oidcUserAuthority.getIdToken();
//					OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
//
//					// Map the claims found in idToken and/or userInfo
//					// to one or more GrantedAuthority's and add it to mappedAuthorities
//
//				} else if (OAuth2UserAuthority.class.isInstance(authority)) {
//					OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
//
//					Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
//
//					// Map the attributes found in userAttributes
//					// to one or more GrantedAuthority's and add it to mappedAuthorities
//
//				}
//			});
//
//			return mappedAuthorities;
//		};
//	}
//	
//	
//	@Bean
//	public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
//		final OidcUserService delegate = new OidcUserService();
//
//		return (userRequest) -> {
//			// Delegate to the default implementation for loading a user
//			OidcUser oidcUser = delegate.loadUser(userRequest);
//
//			OAuth2AccessToken accessToken = userRequest.getAccessToken();
//			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//			for (String authority : accessToken.getScopes()) {
//				mappedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
//			}
//			mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//			// TODO
//			// 1) Fetch the authority information from the protected resource using accessToken
//			// 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
//
//			// 3) Create a copy of oidcUser but use the mappedAuthorities instead
//			oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
//
//			return oidcUser;
//		};
//	}
}
