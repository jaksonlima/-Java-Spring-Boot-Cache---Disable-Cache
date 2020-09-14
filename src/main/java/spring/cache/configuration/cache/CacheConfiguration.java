package spring.cache.configuration.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


@Aspect
@EnableCaching
@Configuration
public class CacheConfiguration implements IDisableCache {
  
  private static final Map<String, Boolean> DISABLE_CAHE = new ConcurrentHashMap();

  @Around("@annotation(DisableCache)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    DISABLE_CAHE.put("disable", Boolean.TRUE);
    return joinPoint.proceed();
  }
  
  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(7, TimeUnit.DAYS);
  }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
      CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
      caffeineCacheManager.setCaffeine(caffeine);
      return caffeineCacheManager;
    }   

    @Bean
    public CacheResolver cacheResolver(CacheManager cacheManager){
        return context -> {
            BasicOperation operation = context.getOperation();

            if(!DISABLE_CAHE.isEmpty() && DISABLE_CAHE.get("disable")){
              Object generateKey = generate(context.getTarget(), context.getMethod(), context.getArgs());

              operation.getCacheNames().stream()
              .map(cacheManager::getCache)
              .forEach(cache -> cache.evictIfPresent(generateKey));
            }

            return operation.getCacheNames()
            .stream()
            .map(cacheManager::getCache)
            .collect(Collectors.toSet());
        };
    }
    
    @Bean
    public KeyGenerator keyGenerator(){
        return (target, method, params) -> {
            return generate(target, method, params);
        };
    }

    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "_"
          + method.getName() + "_"
          + StringUtils.arrayToDelimitedString(params, "_");
    }

	@Override
	public void setDisableCache(boolean disableCache) {
    DISABLE_CAHE.put("disable", disableCache);
	}

}
