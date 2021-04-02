FROM openjdk:11-jdk AS builder
COPY . .
CMD ["./gradlew", "assemble"]
FROM openjdk:11-jdk
COPY --from=builder /app/build/libs/app.jar .
CMD ["java", "-jar", "app.jar"]
