//package com.thecorporateer.influence.security;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackageClasses = AuthenticatedUserService.class)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//	@Autowired
//	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
//
//	// @Autowired
//	// private MySavedRequestAwareAuthenticationSuccessHandler
//	// authenticationSuccessHandler;
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable().headers().frameOptions().sameOrigin().and().exceptionHandling()
//				.authenticationEntryPoint(restAuthenticationEntryPoint).and().authorizeRequests()
//				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().antMatchers("/login").anonymous()
//				.antMatchers("/h2-console/**").anonymous().antMatchers("/**").authenticated().and().formLogin()
//				.successHandler(mySuccessHandler()).failureHandler(new SimpleUrlAuthenticationFailureHandler()).and()
//				.logout();
//	}
//
//	private AuthenticationSuccessHandler successHandler() {
//		return new AuthenticationSuccessHandler() {
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
//					HttpServletResponse httpServletResponse, Authentication authentication)
//					throws IOException, ServletException {
//				httpServletResponse.getWriter().append("OK");
//				httpServletResponse.setStatus(200);
//			}
//		};
//	}
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService);
//		// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
//	}
//
//	@Bean
//	public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler() {
//		return new MySavedRequestAwareAuthenticationSuccessHandler();
//	}
//
//	@Bean
//	public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
//		return new SimpleUrlAuthenticationFailureHandler();
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		// configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://192.168.0.206:4200"));
//		// configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST"));
//		configuration.setAllowCredentials(true);
//		configuration.addAllowedOrigin("*");
//		configuration.addAllowedHeader("*");
//		configuration.addAllowedMethod("OPTIONS");
//		configuration.addAllowedMethod("GET");
//		configuration.addAllowedMethod("POST");
////		configuration.addAllowedMethod("PUT");
////		configuration.addAllowedMethod("DELETE");
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
//}