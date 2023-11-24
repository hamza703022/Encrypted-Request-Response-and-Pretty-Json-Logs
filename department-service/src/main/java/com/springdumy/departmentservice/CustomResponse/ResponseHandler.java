package com.springdumy.departmentservice.CustomResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, boolean success, String message, Object data) {
        GeneralResponse customResponse = new GeneralResponse();
        customResponse.setSuccess(success);
        customResponse.setMessage(message);
        customResponse.setData(data);

        return new ResponseEntity<>(customResponse, status);
    }
}
