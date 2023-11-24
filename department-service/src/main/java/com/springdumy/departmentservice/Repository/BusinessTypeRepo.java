package com.springdumy.departmentservice.Repository;

import com.springdumy.departmentservice.Model.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeRepo extends JpaRepository<BusinessType, Long> {
    BusinessType findByName(String name);
}
