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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.redmic.databaselib.user.model.UserDetailsImpl;
import es.redmic.databaselib.user.repository.UserLoginRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserLoginRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserLoginRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDetailsImpl user = userRepository.findByEmail(email);
		if (user == null) {
			// TODO Reemplazar Exception por una nuestra
			throw new UsernameNotFoundException(String.format("User %s does not exist!", email));
		}
		return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getEnable(), true, true,
				true, user.getRoles());
	}
}
