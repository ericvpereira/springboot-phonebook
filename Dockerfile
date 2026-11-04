FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN chmod +x ./mvnw && ./mvnw -B -ntp clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd --system phonebook && useradd --system --gid phonebook phonebook
COPY --from=build /workspace/target/phonebook-0.0.1-SNAPSHOT.jar /app/app.jar
USER phonebook
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
