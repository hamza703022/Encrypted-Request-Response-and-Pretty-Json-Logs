package com.springdumy.departmentservice.Controller;

import com.springdumy.departmentservice.CustomRequest.BusinessTypeRequest;
import com.springdumy.departmentservice.CustomResponse.ResponseHandler;
import com.springdumy.departmentservice.Model.BusinessType;
import com.springdumy.departmentservice.service.BusinessTypeService;
import com.springdumy.departmentservice.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businessType")
public class BusinessTypeController {


    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private CryptoService cryptoService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveBusinessType(@RequestBody BusinessTypeRequest businessTypeRequest) {


        if (businessTypeRequest.getName() == null || businessTypeRequest.getName().isEmpty())
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Name is required", null);
        if (businessTypeService.findByName(businessTypeRequest.getName()) != null)
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Business Type already exists", null);

        BusinessType businessType = new BusinessType();
        businessType.setName(businessTypeRequest.getName());
        businessType.setDetail(businessTypeRequest.getDetail());
        businessType.setStatus(businessTypeRequest.getStatus());

        BusinessType newBusinessType = businessTypeService.saveBusinessType(businessType);
        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Business Type saved successfully", newBusinessType);

    }

//    @PostMapping("/save")
//    public ResponseEntity<Object> saveBusinessType(@RequestBody String encryptedRequest) throws Exception {
//
//
//        String decryptedRequest = cryptoService.decrypt(encryptedRequest);
//
//        // Parse the decrypted JSON request body
//        ObjectMapper objectMapper = new ObjectMapper();
//        BusinessTypeRequest businessTypeRequest = objectMapper.readValue(decryptedRequest, BusinessTypeRequest.class);
//
//
//        if (businessTypeRequest.getName() == null || businessTypeRequest.getName().isEmpty())
//            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Name is required", null);
//        if (businessTypeService.findByName(businessTypeRequest.getName()) != null)
//            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Business Type already exists", null);
//
//        BusinessType businessType = new BusinessType();
//        businessType.setName(businessTypeRequest.getName());
//        businessType.setDetail(businessTypeRequest.getDetail());
//        businessType.setStatus(businessTypeRequest.getStatus());
//
//        BusinessType newBusinessType = businessTypeService.saveBusinessType(businessType);
//
//
//        if (newBusinessType != null) {
//            // Encrypt the entire JSON response body
//            String encryptedResponse = cryptoService.encrypt(objectMapper.writeValueAsString(newBusinessType));
//            return ResponseEntity.ok("{\"encryptedData\":\"" + encryptedResponse + "\"}");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
////        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Business Type saved successfully", en);
//
//    }
//

    @GetMapping("/getAll")
    public ResponseEntity<Object> getBusinessType() {
        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Business Type retrieved successfully", businessTypeService.findAll());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteBusinessType(@PathVariable Long id) {
        BusinessType businessType = businessTypeService.findById(id);
        if (businessType == null)
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Business Type does not exist", null);

        businessTypeService.deleteBusinessType(businessType);

        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Business Type deleted successfully", null);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateBusinessType(@PathVariable Long id, @RequestBody BusinessTypeRequest businessTypeRequest) {
        BusinessType businessType = businessTypeService.findById(id);
        if (businessType == null)
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Business Type does not exist", null);

        businessType.setName(businessTypeRequest.getName());
        businessType.setDetail(businessTypeRequest.getDetail());
        businessType.setStatus(businessTypeRequest.getStatus());

        BusinessType updatedBusinessType = businessTypeService.saveBusinessType(businessType);

        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Business Type updated successfully", updatedBusinessType);
    }

    @PostMapping("/getEncrypted")
    public ResponseEntity<Object> getEncrypted(@RequestBody String encryptedData) throws Exception {
        String encrypted = cryptoService.encrypt(encryptedData);
        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Encrypted", encrypted);
    }


    @PostMapping("/getDecrypted")
    public ResponseEntity<Object> getDecrypted(@RequestBody String encryptedData) throws Exception {
        String decrypted = cryptoService.decrypt(encryptedData);
        return ResponseHandler.generateResponse(HttpStatus.OK, true, "Decrypted", decrypted);
    }


}
