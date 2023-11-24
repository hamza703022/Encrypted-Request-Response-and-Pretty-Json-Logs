package com.springdumy.departmentservice.CustomResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {
    private boolean success;
    private String message;
    private Object data;


}
