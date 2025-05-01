# syntax=docker/dockerfile:1
################################################################################

# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:21-jdk-alpine as deps
WORKDIR /build

# Copy maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn/
COPY pom.xml .
RUN chmod +x mvnw

# Download dependencies - with scoped cache
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B dependency:go-offline \
    -Dmaven.repo.local=/root/.m2/repository \
    -DskipTests


################################################################################

FROM deps as package

WORKDIR /build

COPY ../src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################


# If reproducibility is important, consider using a specific digest SHA, like
# eclipse-temurin@sha256:99cede493dfd88720b610eb8077c8688d3cca50003d76d1d539b0efc8cca72b4.
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user that the app will run under.
# See https://docs.docker.com/go/dockerfile-user-best-practices/
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy the executable from the "package" stage.
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8080

#ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher"]
ENTRYPOINT [ "java", "--enable-preview", "org.springframework.boot.loader.launch.JarLauncher" ]
