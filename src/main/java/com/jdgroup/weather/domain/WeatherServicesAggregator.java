package com.jdgroup.weather.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.jdgroup.weather.service.IWeatherServiceIntegrator;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixRuntimeException.FailureType;


@Service
public class WeatherServicesAggregator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServicesAggregator.class);
	
	@Autowired
	IWeatherServiceIntegrator weatherServiceIntegrator;
	
	@Cacheable(value = "LocationList")
    @Retryable(maxAttemptsExpression = "#{${max.retry.attempts.locations:2}}")
	public String getLocations(String weatherToken)
	{
		return weatherServiceIntegrator.getLocations(weatherToken);
	}
	
	@Cacheable(value = "StationList")
	public String getStations(String weatherToken, String limit)
	{
		return weatherServiceIntegrator.getStations(weatherToken, limit);
	}
	
	public String locationListFallback(String weatherToken, Throwable e)

    {

        LOGGER.error("hystrix exception in instant activation:  sid: {} | Exception: {}", e);

        throw new HystrixRuntimeException(FailureType.COMMAND_EXCEPTION, null, "Hystrix Command Exception: ", e.getCause(), e);

    }

}
