package com.veralink;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.veralink.core.security.JWTAuthorizationFilter;
import com.veralink.service.KeyService;

@SpringBootApplication
public class VeralinkCoreApplication {

	public static void main(String[] args) {
		try {
			BouncyCastleInitializer.installProvider();
			SpringApplication.run(VeralinkCoreApplication.class, args);
			
			KeyService keyservice = new KeyService();
			String filePath = keyservice.keyStorePath;
			KeyService.createKeyStoreFile(filePath);

			if (keyservice.populateKeyStore(filePath)) {
				System.out.println("-> Keys successfully saved to KeyStore.");
			}
			else {
				System.out.println("-> Error saving keys to KeyStore.");
			}	
			
	  	} catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/user/create").permitAll()
				.antMatchers(HttpMethod.POST, "/api/user/signin").permitAll()
				.anyRequest().authenticated();
		}
	}
	
	public static class BouncyCastleInitializer {
	   private static Provider PROVIDER = null;
	   
	   public static void installProvider() throws Exception {
	       if (PROVIDER != null) return;
	       PROVIDER = new BouncyCastleProvider();
	       Security.insertProviderAt(PROVIDER, 1);
	   }

	   public static void removeProvider() throws Exception {
	       Security.removeProvider(PROVIDER.getName());
	       PROVIDER = null;
	   }
	}
}
