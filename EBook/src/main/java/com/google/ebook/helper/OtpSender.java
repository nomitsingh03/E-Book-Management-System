package com.google.ebook.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.ebook.entity.User;

import jakarta.mail.internet.MimeMessage;

@Component
public class OtpSender {
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(User user, int otp) {
		// TODO Auto-generated method stub
		String from = "singhnomit2002@gmail.com";
		String to = user.getEmail();

		String subject = "OTP-"+otp;
		String content = "Hello "+user.getName()+"<br>" +"<p>We have received a otp request to reset your E-Book Management account Password.<br>"
				+ "<strong>Your OTP is : "+otp+"</strong> </p><br> " + "Thank you,<br>" + "Regards Team<br> E-Book Management";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Smart-Contact");
			helper.setTo(to);
			helper.setSubject(subject);
			

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
