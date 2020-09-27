FROM adoptopenjdk/openjdk11
VOLUME /tmp
ADD build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]