  # Microservice Best Practices


This project could be used for three best practices while writing microservices: 

1. Feign Client : Earlier we used to write lots of code extending the class of RestTemplate whenever we want to make REST calls and manage the request, response and properties used for REST calls. As we know, whenever the lines of code increases, the maintenance of that code becomes a bit more difficult. To cover each of the lines, we need to write junit test cases too. But all these lines of code can be avoided using Feign clients. All we need to write is just an interface with method declarations which should have mapping method and the URL annotated. The properties used for REST call (maximum number of connections, time out values) could be configured through yml or properties file.  

@FeignClient(name="weather", path="/cdo-web/api/v2", url="https://www.ncdc.noaa.gov")
public interface IWeatherServiceIntegrator

2. Exception Handler : We use to have some layers while writing the microservices - application, controller, domain, integration as applicable. Any of these layers might throw some kind of exceptions. MThe same exception could be thrown by multiple methods in multiple layers. If we want to handle it and write that handler for all these methods, it becomes duplicate code. Instead, we can write a common exception handler class with methods handling each kind of exceptions we want to handle in our code so that we can provide a uniform behaviour whenever a particular exception is thrown from any layer. All we need to do is to write the handler class with an annotation @ControllerAdvice. 

@ControllerAdvice
public class WeatherServicesExceptionHandler 

3. Caching : When the backend call is expensive, caching can be used as a very important feature to save backend call if data is not suppossed to be changed frequently. It can be achieved easily by configuring the ehcache in the application layer and annotating the method with @Cacheable whose response we want to cache. The required properties could be placed in yml or properties file under resources.

	@Cacheable(value = "LocationList")
    @Retryable(maxAttemptsExpression = "#{${max.retry.attempts.locations:2}}")
	public String getLocations(String weatherToken)
