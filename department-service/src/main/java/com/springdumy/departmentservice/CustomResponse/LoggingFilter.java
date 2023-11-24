//package com.springdumy.departmentservice.CustomResponse;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//public class LoggingFilter extends OncePerRequestFilter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
//        Long startTime = System.currentTimeMillis();
//
//        // Generate a unique request ID
//        String requestId = UUID.randomUUID().toString();
//
//        // Add the request ID to the request and response
//        contentCachingRequestWrapper.setAttribute("requestId", requestId);
//        contentCachingResponseWrapper.setHeader("requestId", requestId);
//
//        long timeTaken = System.currentTimeMillis() - startTime;
//
//        try {
//            filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
//        } catch (Exception e) {
//            LOGGER.error("Exception occurred while filtering", e);
//        }
//
//        String requestBody = getStringValue(contentCachingRequestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
//        String responseBody = getStringValue(contentCachingResponseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
//
//        LogData logData = new LogData(
//                response.getStatus(),
//                timeTaken,
//                requestId,
//                request.getMethod(),
//                request.getRequestURI(),
//                objectMapper.readTree(requestBody),
//                objectMapper.readTree(responseBody)
//        );
//        if (response.getStatus() == 200) {
//            try {
//                String logJson = objectMapper.writeValueAsString(logData);
//                LOGGER.info(logJson);
//            } catch (JsonProcessingException e) {
//                LOGGER.error("Error serializing log message to JSON", e);
//            }
//
//            contentCachingResponseWrapper.copyBodyToResponse();
//        } else {
//            try {
//                String logJson = objectMapper.writeValueAsString(logData);
//                LOGGER.error(logJson);
//            } catch (JsonProcessingException e) {
//                LOGGER.error("Error serializing log message to JSON", e);
//            }
//
//            contentCachingResponseWrapper.copyBodyToResponse();
//        }
//    }
//
//    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
//        try {
//            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    private static class LogData {
//        private int responseCode;
//        private long timeTaken;
//        private String requestId;
//        private String method;
//        private String requestUri;
//        private Object requestBody;
//        private Object responseBody;
//    }
//}
