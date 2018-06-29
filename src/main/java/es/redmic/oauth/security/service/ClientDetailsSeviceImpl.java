package es.redmic.oauth.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import es.redmic.databaselib.user.model.ClientDetailsImpl;
import es.redmic.databaselib.user.repository.ClientLoginRepository;

@Service
@Qualifier("clientDetailsService")
public class ClientDetailsSeviceImpl implements ClientDetailsService {

	ClientLoginRepository clientRepository;

	@Autowired
	public ClientDetailsSeviceImpl(ClientLoginRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientDetailsImpl client = clientRepository.findByClientId(clientId);
		if (client == null) {
			// TODO Reemplazar Exception por una nuestra
			throw new UsernameNotFoundException(String.format("User %s does not exist!", clientId));
		}
		return new CustomClientDetails(client);
	}

}
