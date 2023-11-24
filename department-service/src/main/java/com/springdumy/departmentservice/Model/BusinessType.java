package com.springdumy.departmentservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "business_type")
@AllArgsConstructor
@NoArgsConstructor
public class BusinessType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String detail;
    private String status;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    private boolean isActive;
    private boolean isDeleted;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "business_type_id")
    private List<BusinessTypeDocuments> documents;


}
