package com.lcwd.electronic.store.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheInspectionService {
	
	Logger logger = LoggerFactory.getLogger(CacheInspectionService.class);

	@Autowired
	private CacheManager cacheManager;
	
	public void printCacheContent(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		
		if(cache!=null) {
			logger.debug("Cache Content = {}", Objects.requireNonNull(cache.toString()));
		}else {
			logger.info("No Such Cache = {} ",cacheName);
		}
	}
}
