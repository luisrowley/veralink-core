package com.veralink;

import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

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
import com.veralink.service.SignatureService;
import com.veralink.service.VerifierService;

import COSE.OneKey;

@SpringBootApplication
public class VeralinkCoreApplication {

	public static void main(String[] args) {
		try {
			BouncyCastleInitializer.installProvider();
			SpringApplication.run(VeralinkCoreApplication.class, args);
			
			String message = "myTest";
			
			KeyPair keyPair = KeyService.generateECKeyPair();
			ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
			ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
			OneKey key = KeyService.generateOneKeyPair(ecPublicKey, ecPrivateKey);
			
			byte[] signedMessage = SignatureService.signCBORMessage(message, key);
			System.out.println(signedMessage);
			System.out.println(VerifierService.validateCoseBytes(signedMessage, key));
			System.out.println(VerifierService.getDecodedCBOR(signedMessage, key));
			// save to .env
			String filePath = "/home/vortex/cacerts/keystore.jks";
			KeyService.createKeyStoreFile(filePath);
			System.out.println(KeyService.populateKeyStore(filePath));
			System.out.println(KeyService.getPrivateKeyFromStore(filePath));
			System.out.println(KeyService.getCertificateFromStore("eckey", filePath));
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
