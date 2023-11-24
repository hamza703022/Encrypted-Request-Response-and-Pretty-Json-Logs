package com.springdumy.departmentservice.Repository;

import com.springdumy.departmentservice.Model.BusinessTypeDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeDocumentRepo extends JpaRepository<BusinessTypeDocuments, Long> {
    BusinessTypeDocuments findByName(String name);
}
