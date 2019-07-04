package com.jdgroup.weather;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.jdgroup.weather.domain.WeatherServicesAggregator;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableCaching
public class WeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}
	
	@Bean
	public WeatherServicesAggregator weatherServicesAggregator()
	{
		return new WeatherServicesAggregator();
	}
	
	public CacheManager ehCacheManager(@Value("${spring.cache.ehcache.maxEntriesLocalHeap=100}") int maxEntriesLocalHeap,
			@Value("${spring.cache.ehcache.timeToIdleSeconds=100}") int timeToIdleSeconds,
			@Value("${spring.cache.ehcache.timeToLiveSeconds=100}") int timeToLiveSeconds,
			@Value("${spring.cache.ehcache.names=LocationList,StationList}") String[] cacheNames)
	{
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		Arrays.asList(cacheNames).forEach(name ->{
			CacheConfiguration cacheConfiguration = new CacheConfiguration();
			cacheConfiguration.name(name);
			cacheConfiguration.maxEntriesLocalHeap(maxEntriesLocalHeap);
			cacheConfiguration.eternal(false);
			cacheConfiguration.timeToIdleSeconds(timeToIdleSeconds);
			cacheConfiguration.timeToLiveSeconds(timeToLiveSeconds);
			cacheConfiguration.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU);
			config.addCache(cacheConfiguration);
		});
		return CacheManager.create(config);
	}

}
