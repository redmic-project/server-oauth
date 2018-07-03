package es.redmic.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@DependsOn("tokenStore")
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

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
