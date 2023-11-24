package com.springdumy.departmentservice.Controller;


import com.springdumy.departmentservice.CustomRequest.BusinessTypeDocumentRequest;
import com.springdumy.departmentservice.CustomResponse.ResponseHandler;
import com.springdumy.departmentservice.Model.BusinessTypeDocuments;
import com.springdumy.departmentservice.service.BusinessTypeDocumentService;
import com.springdumy.departmentservice.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/businessTypeDocument")
public class BusinessTypeDocumentController {


    @Autowired
    private BusinessTypeDocumentService businessTypeDocumentService;

    @Autowired
    private BusinessTypeService businessTypeService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveBusinessTypeDocument(@RequestBody BusinessTypeDocumentRequest businessTypeDocumentRequest){
        if (businessTypeDocumentRequest.getName() == null || businessTypeDocumentRequest.getName().isEmpty())
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false, "Name is required", null);
        if (businessTypeDocumentService.findByName(businessTypeDocumentRequest.getName()) != null)
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false, "Business Type Document already exists", null);

        BusinessTypeDocuments businessTypeDocument = new BusinessTypeDocuments();
        businessTypeDocument.setName(businessTypeDocumentRequest.getName());
        businessTypeDocument.setType(businessTypeDocumentRequest.getType());
        businessTypeDocument.setDetail(businessTypeDocumentRequest.getDetail());
        businessTypeDocument.setStatus(businessTypeDocumentRequest.getStatus());
        businessTypeDocument.setBusinessType(businessTypeService.findById(businessTypeDocumentRequest.getBusinessTypeId()));

        BusinessTypeDocuments newBusinessTypeDocument = businessTypeDocumentService.saveBusinessTypeDocument(businessTypeDocument);
        return ResponseHandler.generateResponse(HttpStatus.OK, true,  "Business Type Document saved successfully", newBusinessTypeDocument);

    }



}
