package spring.cache.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import spring.cache.configuration.cache.Cacheable;
import spring.cache.service.AtomicService;

@Service
public class AtomicImpl implements AtomicService {

  private final AtomicInteger counter = new AtomicInteger(0);

  @Override
  @Cacheable("atomic")
  public Integer increment() {
  	return counter.incrementAndGet();
  }
  
}