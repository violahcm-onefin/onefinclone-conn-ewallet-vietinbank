FROM java:8-jdk-alpine

COPY ./target/conn-ewallet-vietinbank.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch conn-ewallet-vietinbank.jar'

ENTRYPOINT ["java","-jar","conn-ewallet-vietinbank.jar"]
