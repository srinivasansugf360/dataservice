package com.ugf360.dbdemo.springconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

    @Component
    public class CustomItemWriter implements ItemWriter<Object> {

        @Autowired
        private RepositoryFactory repositoryFactory;  // Inject RepositoryFactory

        // @PersistenceContext
        // private EntityManager entityManager;  // Inject EntityManager for batch operations

        // Constructor
        public CustomItemWriter(RepositoryFactory repositoryFactory) {
            this.repositoryFactory = repositoryFactory;
            System.out.println("XXXXXX - CustomItemWriter instantiated - XXXXXX " + repositoryFactory);
        }
        @Override
        public void write(Chunk<? extends Object> chunk) throws Exception {
            // Validate the chunk
            List<? extends Object> items = chunk.getItems();
            if (items == null || items.isEmpty()) {
                return;  // No items to write
            }

            // Get the entity class type from the first item in the chunk
            Class<?> entityClass = items.get(0).getClass();
            System.out.println("XXXXXX - CustomItemWriter Writing items of type: " + entityClass.getName() + " ENTITY CLASS entityClass "+ entityClass);

            // Use RepositoryFactory to get the corresponding repository for the entity class
            JpaRepository<Object, ?> repository = (JpaRepository<Object, ?>) repositoryFactory.getRepository(entityClass);

            // Now you can access the repository and perform actions if needed

            // Check if the repository is found
            if (repository != null) {
                // Persist the items using saveAll() if the repository is found

                repository.saveAll(items);

  // Use EntityManager for batch insert to improve performance (avoid one-by-one saves)
                // for (int i = 0; i < items.size(); i++) {
                //     entityManager.persist(items.get(i));
                    
                //     // Flush and clear periodically to avoid memory overload
                //     if (i % 1000 == 0 && i > 0) {
                //         entityManager.flush();
                //         entityManager.clear();
                //     }
                // }
                // entityManager.flush();
                // entityManager.clear();                

                System.out.println("XXXXXX - Saved " + items.size() + " items of type: " + entityClass.getSimpleName());
            } else {
                // Handle the case where no repository is found
                throw new Exception("Repository not found for class " + entityClass.getName());
            }
        }
    }
