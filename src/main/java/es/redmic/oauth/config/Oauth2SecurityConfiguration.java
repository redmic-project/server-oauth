package es.redmic.oauth.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import es.redmic.oauth.security.service.ClientDetailsSeviceImpl;
import es.redmic.oauth.security.service.UserDetailsServiceImpl;

@Configuration
public class Oauth2SecurityConfiguration {

	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		DataSource dataSource;

		@Bean
		public JdbcTokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}

		@Autowired
		private UserDetailsServiceImpl userDetailsService;

		@Autowired
		private ClientDetailsSeviceImpl clientDetailsService;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

			security.checkTokenAccess("isAuthenticated()");
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			// @formatter:off
			endpoints.tokenStore(tokenStore()).authenticationManager(this.authenticationManager)
					.userDetailsService(userDetailsService);
			// @formatter:on
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			clients.withClientDetails(clientDetailsService);
			// @formatter:on
		}

		@Bean
		@Primary
		public DefaultTokenServices tokenServices() {
			DefaultTokenServices tokenServices = new DefaultTokenServices();
			tokenServices.setSupportRefreshToken(true);
			tokenServices.setTokenStore(tokenStore());
			return tokenServices;
		}
	}

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		private static final String SPARKLR_RESOURCE_ID = "sparklr";

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.tokenStore(tokenStore).resourceId(SPARKLR_RESOURCE_ID);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
			authenticationEntryPoint.setRealmName("springsec/client");
			authenticationEntryPoint.setTypeName("Basic");

			// @formatter:on

			http.anonymous().and().authorizeRequests().antMatchers(HttpMethod.GET, "/oauth/actuator/**").permitAll();

			http.authorizeRequests().antMatchers("/**").authenticated();

			http.httpBasic().authenticationEntryPoint(authenticationEntryPoint).and().antMatcher("/oauth/token")
					.authorizeRequests().antMatchers("/oauth/token").access("IS_AUTHENTICATED_FULLY");

			http.requestMatchers().antMatchers("/**").and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			// @formatter:off
		}
	}

}
