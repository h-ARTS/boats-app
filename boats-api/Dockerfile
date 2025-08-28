FROM gradle:8.10.0-jdk21

WORKDIR /app
EXPOSE 8080

ENV GRADLE_USER_HOME=/home/gradle/.gradle \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -Dspring.output.ansi.enabled=always"

CMD ["/bin/sh","-lc","if [ -x ./gradlew ]; then ./gradlew bootRun --continuous -x test; else gradle bootRun --continuous -x test; fi"]
