FROM nginx:1.21.6-alpine

COPY ./target/site/jacoco /usr/share/nginx/html