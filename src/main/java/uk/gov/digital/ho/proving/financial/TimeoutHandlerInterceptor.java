package uk.gov.digital.ho.proving.financial;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

public class TimeoutHandlerInterceptor extends HandlerInterceptorAdapter {

    private final long timeout;

    public TimeoutHandlerInterceptor(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (timeout != 0) {
            Thread.sleep(timeout);
        }
        return true;
    }
}
