package es.redmic.oauth.security.service;

/*-
 * #%L
 * OAuth
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;

import es.redmic.databaselib.user.model.ClientDetailsImpl;

public class CustomClientDetails implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomClientDetails(ClientDetailsImpl client) {
		this.clientId = client.getClientId();
		this.resourceids = client.getResourceids();
		this.secret = client.getSecret();
		this.scope = client.getScope();
		this.authorizedGrantTypes = client.getAuthorizedGrantTypes();
		this.webServerRedirectUri = client.getWebServerRedirectUri();
		this.accessTokenValidity = client.getAccessTokenValidity();
		this.refreshTokenValidity = client.getRefreshTokenValidity();
		this.autoApprove = client.getAutoApprove();
		this.authorities = client.getAuthorities();
	}

	private String clientId;
	private String resourceids;
	private String secret;
	@SuppressWarnings("unused")
	private String scope;

	private String authorizedGrantTypes;

	private String webServerRedirectUri;

	private Integer accessTokenValidity;

	private Integer refreshTokenValidity;

	private String autoApprove;

	private String authorities;

	public String getResourceids() {
		return resourceids;
	}

	public void setResourceids(String resourceids) {
		this.resourceids = resourceids;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public Integer getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(Integer accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public Integer getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(Integer refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String getAutoApprove() {
		return autoApprove;
	}

	public void setAutoApprove(String autoApprove) {
		this.autoApprove = autoApprove;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientSecret() {
		return secret;
	}

	@Override
	public boolean isScoped() {
		return false;
	}

	@Override
	public Set<String> getScope() {
		return null;
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		return new HashSet<String>(Arrays.asList(authorizedGrantTypes.split(",")));
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(this.authorities);
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		return accessTokenValidity;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValidity;
	}

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAutoApprove(String scopeIn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
