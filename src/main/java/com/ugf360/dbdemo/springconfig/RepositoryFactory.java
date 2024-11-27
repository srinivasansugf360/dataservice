package com.ugf360.dbdemo.springconfig;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.core.GenericTypeResolver;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@Component
// @JobScope
public class RepositoryFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
        public RepositoryFactory(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;

            if (this.applicationContext == null) {
                System.out.println("XXXX applicationContext is null");
            } else {
                System.out.println("XXXX RepositoryFactory instantiated and ApplicationContext is successfully injected");
            }
        }
    /**
     * Dynamically resolves and retrieves the repository for a given entity class.
     * It finds all repositories of type JpaRepository and checks if their domain class matches the provided entity.
     *
     * @param clazz The class of the entity (e.g., U3Entity.class).
     * @return The corresponding repository bean, or null if not found.
     */
    public Object getRepository(Class<?> clazz) {
        // Get all beans of type JpaRepository (these are the repository beans in the Spring context)
        // System.out.println("XXXX - FETCHING REPOSITORY 00 ");
        System.out.println("XXXX repository.getClass() ");

        Map<String, JpaRepository> repositories = applicationContext.getBeansOfType(JpaRepository.class);
        // System.out.println("XXXX - FETCHING REPOSITORY-01 " + repositories + "... repositories.values()=" + repositories.values());

        // Loop through all repositories to find the one associated with the given entity class
        for (JpaRepository repository : repositories.values()) {
            // Use reflection to get the domain class type from the repository
            System.out.println("XXXX repository.getClass() "+ repository.getClass());
            Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(repository.getClass(), JpaRepository.class);

            // genericTypes should have two elements: [T, ID]
            if (genericTypes != null && genericTypes.length == 2) {
                Class<?> domainClass = genericTypes[0];  // This will be U3Entity in your case
                if (domainClass.equals(clazz)) {
                    return repository;
                }
            }
        }
        // Return null if no repository is found for the given entity class
        return null;
    }
}
