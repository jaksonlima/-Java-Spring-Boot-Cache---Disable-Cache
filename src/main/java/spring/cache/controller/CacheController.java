package spring.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.cache.configuration.cache.DisableCache;
import spring.cache.service.AtomicService;

@RestController
public class CacheController {

 @Autowired
 private AtomicService atomicService;

  @DisableCache
  @GetMapping("/disabled/increment")
  public int disabled() {
      return atomicService.increment();
  }

  @GetMapping("/enabled/increment")
  public int enabled() {
      return atomicService.increment();
  }
  
}