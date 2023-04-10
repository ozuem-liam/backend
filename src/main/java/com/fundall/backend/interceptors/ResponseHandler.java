package com.fundall.backend.interceptors;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

   
    @Override
    public Object beforeBodyWrite(
        @Nullable Object body, 
        MethodParameter returnType, 
        MediaType selectedContentType, 
        Class<? extends HttpMessageConverter<?>> selectedConverterType, 
        ServerHttpRequest request, 
        ServerHttpResponse response
    ) {
        
        if (body instanceof ApiResponse) {
            return body;
        }
        
        return ApiResponse.success(body);
    }
}
