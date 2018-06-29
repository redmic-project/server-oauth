package es.redmic.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import es.redmic.databaselib.common.repository.BaseRepositoryImpl;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({ "es.redmic.oauth" })
@EntityScan({ "es.redmic.databaselib" })
@EnableJpaRepositories(basePackages = {
		"es.redmic.databaselib.user.repository" }, repositoryBaseClass = BaseRepositoryImpl.class)
public class OauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthApplication.class, args);
	}
}
