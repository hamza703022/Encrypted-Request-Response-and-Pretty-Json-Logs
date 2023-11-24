package com.springdumy.departmentservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "business_type_documents")
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTypeDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String detail;
    private String status;
    private String type;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate;
    private boolean isActive;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "business_type_id")
    private BusinessType businessType;

}