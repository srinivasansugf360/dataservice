package com.ugf360.datastore.rfp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "U3_Entity")
public class U3Entity {

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    // @SequenceGenerator(name = "seq_gen", sequenceName = "my_sequence")    
    private String stUeCode;  // Company Code
    private String stUeName;  // Company Name
    private String stUeCountryCode;  // Country
    private String stUeRegionCode;  // Region
    private String stUeStatusCode;  // Status
    private String stUeCategoryCode;  // Category

    // Getters and Setters
    public String getStUeCode() {
        return stUeCode;
    }

    public void setStUeCode(String stUeCode) {
        this.stUeCode = stUeCode;
    }

    public String getStUeName() {
        return stUeName;
    }

    public void setStUeName(String stUeName) {
        this.stUeName = stUeName;
    }

    public String getStUeCountryCode() {
        return stUeCountryCode;
    }

    public void setStUeCountryCode(String stUeCountryCode) {
        this.stUeCountryCode = stUeCountryCode;
    }

    public String getStUeRegionCode() {
        return stUeRegionCode;
    }

    public void setStUeRegionCode(String stUeRegionCode) {
        this.stUeRegionCode = stUeRegionCode;
    }

    public String getStUeStatusCode() {
        return stUeStatusCode;
    }

    public void setStUeStatusCode(String stUeStatusCode) {
        this.stUeStatusCode = stUeStatusCode;
    }

    public String getStUeCategoryCode() {
        return stUeCategoryCode;
    }

    public void setStUeCategoryCode(String stUeCategoryCode) {
        this.stUeCategoryCode = stUeCategoryCode;
    }
}
