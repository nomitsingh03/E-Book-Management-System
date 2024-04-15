package com.google.ebook.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.ebook.entity.Address;
import com.google.ebook.entity.User;
import com.google.ebook.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	@Value("${spring.mail.username}")
	private String email;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private BCryptPasswordEncoder passworEncoder;
	
	public static final long ATTEMPT_TIME=3;

	public User saveUser(User user, String url) {
		// TODO Auto-generated method stub
		String password = passworEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole("ROLE_USER");
		user.setEnable(false);
		user.setVerificationCode(UUID.randomUUID().toString());
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);
		user.setLockTime(null);
		User newUser = userRepo.save(user);
		if (newUser != null) {
			sendEmail(newUser, url);
		}

		return newUser;
	}

	public void sendEmail(User user, String url) {

		// Enter your email
		String from = email;
		String to = user.getEmail();

		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>"
				+ "Regards E-Book Management";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "E-Book Store");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getName());
			String siteUrl = url + "/registration/verify?code=" + user.getVerificationCode();
			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean verifyAccount(String verificationCode) {

		User user = userRepo.findByVerificationCode(verificationCode);
		if (user == null)
			return false;
		else {
			user.setEnable(true);
			user.setVerificationCode(null);
			userRepo.save(user);
			return true;
		}
	}
	

	public void increaseFailedAttempt(User user) {
		// TODO Auto-generated method stub
		int attempt = user.getFailedAttempt()+1;
		userRepo.updateFailedAttempt(attempt, user.getEmail());
	}
	
	private static final long lock_durationTime = 60000;

	
	public void resetAttempt(String email) {
		userRepo.updateFailedAttempt(0, email);
	}


	public void lock(User user) {
		
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepo.save(user);
	}

	public boolean unLockAccountTimeExpired(User user) {
		
		long lockTimeInMills = user.getLockTime().getTime();
		long currentTimeInMills = System.currentTimeMillis();
		
		if(lockTimeInMills+lock_durationTime < currentTimeInMills) {
			user.setAccountNonLocked(true);
			user.setLockTime(null);
			user.setFailedAttempt(0);
			userRepo.save(user);
			return true;
		}
		return false;
	}
	
	public boolean userExist(User user) {
		User registeredUser = userRepo.findByEmail(user.getEmail());
		if(registeredUser!=null) return true;
		return false;
	}
	
	public Address getAddress(User user) {
		Address address = null;
		List<Address> oldAddresses = user.getAddress();
		for(Address add: oldAddresses) {
			if(add.isDef()) address=add;
		}
		if(address==null && !oldAddresses.isEmpty())address= oldAddresses.get(oldAddresses.size()-1);
		return address;
	}

}
