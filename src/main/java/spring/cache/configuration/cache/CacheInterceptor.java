package spring.cache.configuration.cache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("handlerCacheInterceptor")
public class CacheInterceptor implements HandlerInterceptor {

  @Autowired
  private IDisableCache disableCache;
  
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    disableCache.setDisableCache(false);
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }
}
