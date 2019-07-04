package com.jdgroup.weather.exceptions;

import java.io.IOException;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class ServiceExceptionDecoder implements ErrorDecoder 

{
	
	private ErrorDecoder delegate = new ErrorDecoder.Default();

	@Override
	public Exception decode(String methodKey, Response response) 
	{
		if (response.status() == 400)
		{
			try {
				byte[] bodyData = Util.toByteArray(response.body().asInputStream());
				String responseBody = new String(bodyData);
				System.out.println("Exception from ServiceExceptionDecoder with status: " + response.status() + 
						" and response: " + responseBody);
				return new HystrixBadRequestException(responseBody);
			} catch (IOException e) {
				System.out.println("Exception from ServiceExceptionDecoder with status: " + response.status() + 
						" and exception: " + e);
				return new HystrixBadRequestException(e.getMessage());
			}
			
		}
		return delegate.decode(methodKey, response);
	}
	
	

}
