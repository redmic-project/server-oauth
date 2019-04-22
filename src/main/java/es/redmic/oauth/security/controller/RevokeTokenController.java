package es.redmic.oauth.security.controller;

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
