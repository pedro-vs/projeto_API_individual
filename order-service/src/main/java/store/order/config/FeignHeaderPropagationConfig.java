package store.order.config;

import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignHeaderPropagationConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            copyHeader(request, requestTemplate, "id-account");
            copyHeader(request, requestTemplate, "Authorization");
        };
    }

    private void copyHeader(HttpServletRequest request, feign.RequestTemplate template, String headerName) {
        String headerValue = request.getHeader(headerName);
        if (headerValue != null && !headerValue.isBlank()) {
            template.header(headerName, headerValue);
        }
    }
}
