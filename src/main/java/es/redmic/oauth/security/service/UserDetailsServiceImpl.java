package es.redmic.oauth.security.service;

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
