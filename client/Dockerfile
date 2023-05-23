FROM --platform=linux/amd64 maven as builder
WORKDIR /project
COPY . /project/
RUN mvn clean package -DskipTests -B


FROM --platform=linux/amd64 openjdk:17-alpine

WORKDIR /application

COPY --from=builder /project/target/*.jar /application/app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_OPTS -jar /application/app.jar $JAVA_CONFIGS"]
