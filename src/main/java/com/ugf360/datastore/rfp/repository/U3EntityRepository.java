package com.ugf360.datastore.rfp.repository;

import com.ugf360.datastore.rfp.entity.U3Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface U3EntityRepository extends JpaRepository<U3Entity, String> {
    U3Entity findByStUeCode(String stUeCode);
}
