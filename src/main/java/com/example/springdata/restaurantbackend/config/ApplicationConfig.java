package com.example.springdata.restaurantbackend.config;

import com.example.springdata.restaurantbackend.Repository.UtilisateurRepository;
import com.sinch.sdk.domains.sms.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import com.sinch.sdk.models.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.sinch.sdk.SinchClient;
import java.util.ArrayList;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UtilisateurRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("choeurproject@gmail.com");
        mailSender.setPassword("oabw kzbc bghm mgeb");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public SMSService sinchClient() {
        Configuration configuration = com.sinch.sdk.models.Configuration.builder().setKeyId("cd631b87-cf37-4c62-bbee-589c346e5ff0")
                .setKeySecret("QuMLzX4Bmf4lWb1RHZ7iRHdT5F")
                .setProjectId("67bc077c-8572-4cf8-a2bb-38c1184c4b1b")
                .build();

        return
                new SinchClient(configuration).sms();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)

                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getMotDePasse(),
                        new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
