package es.redmic.oauth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import es.redmic.oauth.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEventPublisher authenticationEventPublisher;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public static PasswordEncoder passwordEncoder() {

		String idForEncode = "bcrypt";
		Map encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder(10));

		DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(idForEncode, encoders);
		encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder(10));

		return encoder;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		auth.authenticationEventPublisher(authenticationEventPublisher);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setForcePrincipalAsString(false);
		return authProvider;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/oauth/uncache_approvals", "/oauth/cache_approvals");
	}

	@Override
	protected void configure(HttpSecurity http) {

		try {
			http.headers()
					.addHeaderWriter(
							new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
					.and().requestMatchers().antMatchers("/*/").and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		} catch (Exception e) {
			// throw new SecurityConfigurationException("Error al cargar las
			// reglas de acceso a los servicios", e);
		}
	}
}
