FROM amazoncorretto:17 as build

LABEL maintainer = "Pavel <front-91@mail.ru>"

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

FROM amazoncorretto:17

VOLUME /tmp

ARG DEPENDENCY=/target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.quick.recording.user.service.UserServiceApplication"]