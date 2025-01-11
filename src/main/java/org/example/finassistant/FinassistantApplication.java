package org.example.finassistant;

import org.example.finassistant.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class FinassistantApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinassistantApplication.class, args);

	}

}
