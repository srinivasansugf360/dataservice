package com.ugf360.dbdemo.springconfig;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Value;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.batch.core.ExitStatus;

import com.ugf360.dbdemo.config.EntityMapping;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class CustomItemProcessor implements ItemProcessor<Map<String, Object>, Object>, StepExecutionListener {

    private StepExecution stepExecution;
    private String className;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;  // Capture StepExecution before the step begins
    }

    @Override
    public Object process(Map<String, Object> item) throws Exception {

        // Get job parameters to retrieve the entityKey
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String entityKey = jobParameters.getString("entityKey");

        if (entityKey == null) {
            throw new IllegalArgumentException("Entity key is missing in job parameters.");
        }

        // Get the EntityConfiguration using the entityKey
        EntityMapping.EntityConfiguration entityConfig = EntityMapping.getEntityConfiguration(entityKey);

        // Get the entity class and repository class from the EntityConfiguration
        Class<?> entityClass = entityConfig.getEntityClass();

        // Dynamically instantiate the entity class
        Object entityInstance = entityClass.getDeclaredConstructor().newInstance();

        // Iterate over the map (item) and populate fields of the entity object
        for (Map.Entry<String, Object> entry : item.entrySet()) {
            String fieldName = entry.getKey();
            Object valueToSet = entry.getValue();

            // Skip setting the field if the value is null
            if (valueToSet == null) {
                continue;
            }

            try {
                // Get the field from the entity class
                Field field = entityClass.getDeclaredField(fieldName);

                field.setAccessible(true);  // Make private fields accessible

                // Handle XSSFCell if valueToSet is an Excel cell
                if (valueToSet instanceof XSSFCell) {
                    XSSFCell cell = (XSSFCell) valueToSet;

                    // Determine the correct cell type and extract the value
                    switch (cell.getCellType()) {
                        case STRING:
                            valueToSet = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            valueToSet = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            valueToSet = cell.getBooleanCellValue();
                            break;
                        default:
                            valueToSet = null;  // Handle unsupported cell types gracefully
                            break;
                    }
                }

                // Set the value to the corresponding field in the entity
                field.set(entityInstance, valueToSet);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Log or handle the exception gracefully
                System.err.println("Error setting field " + fieldName + " in class " + entityClass.getName());
            }
        }

        return entityInstance;  // Return the populated entity object
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
       }
}
