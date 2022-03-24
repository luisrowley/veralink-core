package com.veralink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VeralinkCoreApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(VeralinkCoreApplication.class, args);
	  	} catch (Exception e) {
	        e.printStackTrace(); 
	    }
	}

}
