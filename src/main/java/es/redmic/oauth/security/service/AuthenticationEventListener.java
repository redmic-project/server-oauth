package es.redmic.oauth.security.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

	@Autowired
	EntityManager em;

	@Transactional
	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent ev) {

		em.createNativeQuery("SELECT * from app.login(?)").setParameter(1, ev.getAuthentication().getName())
				.getResultList();
	}
}