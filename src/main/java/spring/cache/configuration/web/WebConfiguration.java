package spring.cache.configuration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import spring.cache.configuration.cache.CacheInterceptor;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

  @Autowired
  private CacheInterceptor cacheInterceptor;

  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(cacheInterceptor);
  }
  
}