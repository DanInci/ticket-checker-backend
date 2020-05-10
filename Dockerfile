FROM adoptopenjdk/openjdk8:debian as build-container

ARG SBT_VERSION=1.3.8

RUN curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
    dpkg -i sbt-$SBT_VERSION.deb && \
    rm sbt-$SBT_VERSION.deb && \
    apt-get update && \
    apt-get install sbt

COPY . /build

WORKDIR /build

RUN sbt mkJar

FROM adoptopenjdk/openjdk8:debian-jre as server-container

COPY --from=build-container /build/ticket-checker-server/target/pack/ /app/

WORKDIR /app

EXPOSE 8080

ENTRYPOINT [ "./bin/ticket-checker-server", "start-server" ]