package com.springdumy.departmentservice.service;


import com.springdumy.departmentservice.Model.BusinessType;
import com.springdumy.departmentservice.Repository.BusinessTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessTypeService {

    @Autowired
    private BusinessTypeRepo businessTypeRepo;

    @Autowired
    private CryptoService cryptoService;


    public BusinessType saveBusinessType(BusinessType businessType) {
        return businessTypeRepo.save(businessType);
    }

    public BusinessType findByName(String name) {
        return businessTypeRepo.findByName(name);
    }

    public List<BusinessType> findAll() {
        return businessTypeRepo.findAll();
    }

    public BusinessType findById(Long id) {
        return businessTypeRepo.findById(id).orElse(null);
    }

    public void deleteBusinessType(BusinessType businessType) {
        businessTypeRepo.delete(businessType);
    }
}
