package com.example.RoomReservationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableAsync
public class RoomReservationSystemApplication {

	public static void main(String[] args) {
		//System.out.println(org.hibernate.Version.getVersionString());
		SpringApplication.run(RoomReservationSystemApplication.class, args);
	}
}
