## Running the application:

Navigate to application root folder and execute the below commands in CLI

step 1:  mvn clean install

step 2:  mvn spring-boot:run

Application starts at 8080 port.


## Swagger 

http://localhost:8080/swagger-ui.html

Provide X-CLIENT-ID as TARGET while testing GET & PUT product API's.

![Arch](/src/main/resources/static/Swagger.png)

## Monitor Application stats

Use the following Spring Actuator Url http://localhost:8089/monitor to get various monitoring links.


## Hystrix dashboard

Open Hystrix home page http://localhost:8080/hystrix and enter the following hystrix monitoring stream url : http://localhost:8089/monitor/hystrix.stream

![Arch](/src/main/resources/static/Hystrix.png)


## Technologies used:

*Java8
*Maven
*Spring Boot
*Orika Mapping
*Embedded Mongo DB
*EhCache
*Hystrix Circuit Breaker
*Swagger2
*Rx Java