package es.redmic.oauth.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.redmic.exception.databinding.DTONotValidException;
import es.redmic.oauth.common.dto.TokenDTO;

@RestController
public class RevokeTokenController {

	@Autowired
	@Qualifier("consumerTokenServices")
	ConsumerTokenServices tokenServices;

	@RequestMapping(value = "${controller.mapping.revokeToken}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revokeToken(@Valid @RequestBody TokenDTO token, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			throw new DTONotValidException(bindingResult);
		
		tokenServices.revokeToken(token.getToken());
	}
}
