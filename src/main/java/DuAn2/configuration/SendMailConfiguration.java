package DuAn2.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SendMailConfiguration {

	@Bean(name = "mailSender")
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		Properties mailProperties = new Properties();

		javaMailSender.setHost("smtp.mailtrap.io");
		javaMailSender.setPort(587);
		javaMailSender.setProtocol("smtp");

		//nhap username gmail
		javaMailSender.setUsername("791500d60d71b4");
		//password gmail
		javaMailSender.setPassword("5c6260b47760ed");
		javaMailSender.setDefaultEncoding("UTF-8");

		mailProperties.put("mail.smtp.auth", "true");
		mailProperties.put("mail.smtp.starttls.enable", "true");
		mailProperties.put("mail.smtp.starttls.required","true");
		mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		mailProperties.put("mail.smtp.debug", "true");
		mailProperties.setProperty("mail.smtp.allow8bitmime", "true");
		mailProperties.setProperty("mail.smtps.allow8bitmime", "true");
	    // Có thể giữ debug nếu cần log
	    mailProperties.put("mail.debug", "true");

		javaMailSender.setJavaMailProperties(mailProperties);
		return javaMailSender;
	}
}
