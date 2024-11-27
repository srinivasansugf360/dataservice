package com.ugf360.dbdemo.config;

import com.ugf360.datastore.rfp.entity.U3Entity;
import com.ugf360.datastore.rfp.repository.U3EntityRepository;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
// @JobScope
public class EntityMapping {

  public EntityMapping () {
    System.out.println("XXXXX EntityMapping instantiated");
  }

    // A static map to hold class name mappings
    private static final Map<String, EntityConfiguration> entityMap = new HashMap<>();

    static {
        // Populate the map with keys (entity type) and their corresponding configurations
        System.out.println("XXXXXX- Entitymapping static");
        entityMap.put("U3ENTITY", new EntityConfiguration(com.ugf360.datastore.rfp.entity.U3Entity.class));

        // Add other entity configurations here as needed
    }

    // Get configuration based on the key
    public static EntityConfiguration getEntityConfiguration(String key) {
        return entityMap.get(key);
    }

    // Inner class to hold entity configuration
    public static class EntityConfiguration {
        private Class<?> entityClass;
        //private Class<? extends JpaRepository> repositoryClass;

        // Constructor to initialize entity class and repository
        public EntityConfiguration(Class<?> entityClass //, Class<? extends JpaRepository> repositoryClass
        ) {
            this.entityClass = entityClass;
            // this.repositoryClass = repositoryClass;
        }

        public Class<?> getEntityClass() {
            return entityClass;
        }

        // public Class<? extends JpaRepository> getRepositoryClass() {
        //     return repositoryClass;
        // }
    }
}
