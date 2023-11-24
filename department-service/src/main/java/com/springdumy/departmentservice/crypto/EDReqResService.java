//package com.springdumy.departmentservice.crypto;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springdumy.departmentservice.service.CryptoService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ReadListener;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletInputStream;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.*;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class EDReqResService extends OncePerRequestFilter {
//
//    private static final Set<String> EXCLUDED_APIS = Set.of("/businessType/getDecrypted", "/businessType/getEncrypted");
//    @Autowired
//    private CryptoService cryptoService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (EXCLUDED_APIS.contains(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        InputStream inputStream = request.getInputStream();
//        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
//                .lines()
//                .collect(Collectors.joining("\n"));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(requestBody);
//        jsonNode = jsonNode.get("encryptedData");
//        log.info("Encrypted Data: " + jsonNode.asText());
//        JsonNode decryptedJsonNode;
//        try {
//            decryptedJsonNode = cryptoService.decrypt(jsonNode.asText(), JsonNode.class);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        log.info("Decrypted Data: " + decryptedJsonNode.toString());
//
//        DecryptedRequestWrapper decryptedRequestWrapper = new DecryptedRequestWrapper(request, decryptedJsonNode.toString());
//        filterChain.doFilter(decryptedRequestWrapper, response);
//    }
//
//    private static class DecryptedRequestWrapper extends HttpServletRequestWrapper {
//        private final String decryptedContent;
//
//        public DecryptedRequestWrapper(HttpServletRequest request, String decryptedContent) {
//            super(request);
//            this.decryptedContent = decryptedContent;
//        }
//
//        @Override
//        public ServletInputStream getInputStream() throws IOException {
//            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedContent.getBytes(getRequest().getCharacterEncoding()));
//            return new ServletInputStream() {
//                @Override
//                public boolean isFinished() {
//                    return false;
//                }
//
//                @Override
//                public boolean isReady() {
//                    return false;
//                }
//
//                @Override
//                public void setReadListener(ReadListener readListener) {
//                    // No-op for synchronous processing
//                }
//
//                @Override
//                public int read() throws IOException {
//                    return byteArrayInputStream.read();
//                }
//            };
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(new InputStreamReader(this.getInputStream(), getRequest().getCharacterEncoding()));
//        }
//    }
//}


package com.springdumy.departmentservice.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springdumy.departmentservice.service.CryptoService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on: 11/22/2023
 * Author: Muhammad Hamza
 */
@Component
@Slf4j
public class EDReqResService extends OncePerRequestFilter {

    private static final Set<String> EXCLUDED_APIS = Set.of("/businessType/getDecrypted", "/businessType/getEncrypted");
    @Autowired
    private CryptoService cryptoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        log.info("Request URI: " + request.getRequestURI());
        if (EXCLUDED_APIS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        InputStream inputStream = request.getInputStream();

        // Step 2: Read the InputStream to obtain the request body
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // Step 3: Parse the request body as JSON (assuming you have a JSON library, like Jackson)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        jsonNode = jsonNode.get("encryptedData");
        log.info("Encrypted Data: " + jsonNode.asText());
        String decryptedJsonNode;
        try {
            decryptedJsonNode = cryptoService.decrypt(jsonNode.asText());
        } catch (Exception e) {
            log.error("Error decrypting JSON", e);
            throw new RuntimeException(e);
        }

        request.setAttribute("decryptedData", decryptedJsonNode.toString());

        log.info("Decrypted Data: " + decryptedJsonNode.toString());


        DecryptedRequestWrapper decryptedRequestWrapper = new DecryptedRequestWrapper(request, decryptedJsonNode.toString());

        JsonNode encryptedJsonNode;
        try {
            OutputStream outputStream = response.getOutputStream();
            String encryptedResponse = cryptoService.encrypt(outputStream.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        EncryptedResponseWrapper encryptedResponseWrapper = new EncryptedResponseWrapper(response);
        filterChain.doFilter(decryptedRequestWrapper, encryptedResponseWrapper);
        byte[] encryptedResponseBytes = encryptedResponseWrapper.getBytes();
        try {
            String encryptedResponse = cryptoService.encrypt(new String(encryptedResponseBytes, StandardCharsets.UTF_8));

            // Build the JSON response with the encrypted value
            String jsonResponse = String.format("{\"encryptedResponse\": \"%s\"}", encryptedResponse);

            // Send the encrypted response back to the client
            response.getOutputStream().write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting the response", e);
        }


    }


    private static class DecryptedRequestWrapper extends HttpServletRequestWrapper {
        private final String decryptedContent;

        public DecryptedRequestWrapper(HttpServletRequest request, String decryptedContent) {
            super(request);
            this.decryptedContent = decryptedContent;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedContent.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }


    private static class EncryptedResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private final PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);

        public EncryptedResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return printWriter;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) throws IOException {
                    byteArrayOutputStream.write(b);
                }
            };
        }

        public byte[] getBytes() {
            printWriter.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }
}

