package com.veralink;

import java.security.Provider;
import java.security.Security;
import java.util.Collections;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.veralink.core.security.JWTAuthorizationFilter;
import com.veralink.service.KeyService;

import io.github.cdimascio.dotenv.Dotenv;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
	
	@Configuration
	@EnableSwagger2
	public class SpringFoxConfig {
		private final Dotenv dotenv = Dotenv.configure().load();
		private final String API_TITLE = dotenv.get("META_TITLE");
		private final String API_DESCRIPTION = dotenv.get("META_DESCRIPTION");
		private final String API_URL_ROOT = dotenv.get("META_URL_ROOT");
		private final String API_CONTACT_EMAIL = dotenv.get("META_CONTACT_EMAIL");
		private final String API_LICENCE_URL = dotenv.get("META_LICENCE_URL");

		private ApiInfo metadata(){
		    return new ApiInfo(
		    	API_TITLE,
		    	API_DESCRIPTION,
				"1.0",
				API_URL_ROOT,
				new Contact("Veralink", API_URL_ROOT, API_CONTACT_EMAIL),
				"MIT LICENSE",
				API_LICENCE_URL,
				Collections.emptyList()
				);
		}

	    @Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .select()                                  
	          .apis(RequestHandlerSelectors.basePackage("com.veralink.controller"))              
	          .paths(PathSelectors.any())                          
	          .build()
	          .apiInfo(metadata());                                           
	    }
	    
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		private final Dotenv dotenv = Dotenv.configure().load();
		private final boolean isProd = Boolean.parseBoolean(dotenv.get("PROD"));
		private final String[] EMPTY_ARRAY = new String[0];
		private final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
	    };

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/user/create").permitAll()
				.antMatchers(HttpMethod.POST, "/api/user/signin").permitAll()
				.antMatchers(isProd ? EMPTY_ARRAY : AUTH_WHITELIST).permitAll()
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
