package com.springdumy.departmentservice.CustomRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BusinessTypeDocumentRequest {
    private String name;
    private String detail;
    private String status;
    private String type;
    private Long businessTypeId;
}
