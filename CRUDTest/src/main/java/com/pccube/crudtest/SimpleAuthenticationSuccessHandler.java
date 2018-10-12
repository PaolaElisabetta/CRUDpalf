package com.pccube.crudtest;
import java.io.IOException;

import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final Logger log = LoggerFactory.getLogger(SimpleAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
			throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		authorities.forEach(authority -> {
			if(authority.getAuthority().equals("user")) {
				try {
					redirectStrategy.sendRedirect(arg0, arg1, "/homeUser");
				} catch (Exception e) {
					log.error("errore");
				}
			} else if(authority.getAuthority().equals("admin")) {
				try {
					redirectStrategy.sendRedirect(arg0, arg1, "/homeAdmin");
				} catch (Exception e) {
				
					log.error("errore");
				}
			} else {
	            throw new IllegalStateException();
	        }
		});
		
	}
 
}