package com.springdumy.departmentservice.service;

import com.springdumy.departmentservice.Model.BusinessTypeDocuments;
import com.springdumy.departmentservice.Repository.BusinessTypeDocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessTypeDocumentService {


    @Autowired
    private BusinessTypeDocumentRepo businessTypeDocumentRepo;


    public BusinessTypeDocuments findByName(String name) {
        return businessTypeDocumentRepo.findByName(name);
    }

    public BusinessTypeDocuments saveBusinessTypeDocument(BusinessTypeDocuments businessTypeDocument) {
        return businessTypeDocumentRepo.save(businessTypeDocument);
    }
}
