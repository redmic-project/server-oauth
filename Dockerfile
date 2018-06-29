FROM redmic/redmic-server

COPY /dist/*.jar ./

EXPOSE 8081

ENTRYPOINT java $JAVA_OPTS \
	-Djava.security.egd=file:/dev/./urandom \
	-Dlogging.level.org.springframework=${LOG_LEVEL} \
	-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
	-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
	-jar ${DIRPATH}/oauth.jar
