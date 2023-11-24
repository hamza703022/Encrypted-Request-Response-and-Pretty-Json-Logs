package com.springdumy.departmentservice.CustomRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class BusinessTypeRequest {

    private String name;
    private String detail;
    private String status;
}
