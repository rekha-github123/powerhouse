Circuit-Breaker-Consumer is a Spring Boot Application Microservice using gradle as build management.

This component is build to achieve Circuit Breaker Pattern POC using jhalterman's Failsafe API.

This spring boot application will invoke external system (rest end point), in POC , we are using Circuit-Breaker-Producer as external System. If external system will up then it will return the results from rest end point. If external system is down or timeout occur during connecting/executing, it will return from fallBack method configure for Circuit Breaker service.

How to run Circuit-Breaker-Consumer:

1. Get Circuit-Breaker-Consumer module from git hub.
2. Go to root folder and run below command to build and run spring boot application. gradle build bootRun
3. Spring boot application will be up and ready to access on port : 8181
4. Port can be changed in /src/main/resources/application.properties file by modifying property server.port=xxxx
5. Test the application and its exposed rest service at below location 
	
	http://localhost:8181/test

Demo URL for Failsafe Circuit Breaker :

1. Bring Circuit-Breaker-Producer application up as mention in the module.
2. Bring Circuit-Breaker-Consumer application up and running.
3. Use below Http call from browser.

	1. http://localhost:8181/withoutCB
	
       Above Get call is implemented to show the behavior of Service without Circuit Breaker. It will return the result if external system is up. It will fail with 500 error when external system (Circuit-Breaker-Producer) is down. You can bring down the external system instance and check the response.

	2. http://localhost:8181/withCB
	
	Follow the same steps and check the behavior in this case. This service will return response from FallBack Method if external system is down as the call is wrapped within the Failsafe circuit breaker.
	
	3. http://localhost:8181/withCbRetry	
	This call is added to show the behavior of Failsafe Retry feature. it will retry for 5 times at delay of 2 sec if it get NULL response. If the producer system is down then the response will come from fallback method.all the monitoring details will be printed on console related to circuit state and retry.
	
If connection fails for the configured threshold, circuit will be in open state and it will never try to establish the connection. It always return from FallBack method.

Once circuit is in closed state it will establish the connection and return the results from external system.





 
