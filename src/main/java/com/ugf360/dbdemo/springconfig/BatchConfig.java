package com.ugf360.dbdemo.springconfig;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.Map;

@Component
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CustomItemReader customItemReader;

    @Autowired
    private CustomItemProcessor customItemProcessor;

    @Autowired
    private CustomItemWriter customItemWriter;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public BatchConfig() {
        System.out.println("XXXXXX- Batch config instantiated-XXXXXX");
    }

    // Define a ThreadPoolTaskExecutor for multi-threading
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Number of threads
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Batch-Thread-");
        return executor;
    }

    // Method to create a job with dynamic configuration
    public Job createDynamicJob(JobParameters jobParameters) {
        System.out.println("XXXXXXXXXX- createDynamicJob() IS CALLED");

        // Determine file type and set up the appropriate reader, processor, and writer
        String fileType = jobParameters.getString("fileType");
        String entityKey = jobParameters.getString("entityKey");

        return new JobBuilder("dynamicJob", jobRepository)
              .start(dynamicStep()) // Create step dynamically
              .build();
    }

    // Dynamic step creation
    // public Step dynamicStep() {
    //     return new StepBuilder("dynamicStep", jobRepository)
    //             .<Map<String, Object>, Object>chunk(1000, transactionManager)
    //             .reader(dynamicItemReader())        // Dynamic reader based on file type
    //             .processor(dynamicItemProcessor()) // Dynamic processor based on entity
    //             .writer(dynamicItemWriter())                // Custom writer (e.g., database writer)
    //             .transactionManager(transactionManager)
    //             .build();

    public Step dynamicStep() {
        return new StepBuilder("dynamicStep", jobRepository)
                .<Map<String, Object>, Object>chunk(1000, transactionManager) // Set chunk size
                .reader(customItemReader) // Use the dynamic reader
                .processor(customItemProcessor) // Use the dynamic processor
                .writer(customItemWriter) // Use the custom writer
                .taskExecutor(taskExecutor()) // Enable multi-threading using task executor
                .throttleLimit(10) // Limit the number of concurrent threads to 10
                .transactionManager(transactionManager) // Ensure the transaction manager is used
                .build();                
    }
    // // Dynamically choose the ItemReader based on file type
    // public ItemReader<Map<String, Object>> dynamicItemReader() {
    //         return customItemReader;
    // }

    // // Dynamically create the ItemProcessor based on entityKey
    // public ItemProcessor<Map<String, Object>, Object> dynamicItemProcessor() {
    //     System.out.println("Dynamic Processor for entityKey: ");
    //     return customItemProcessor;
    // }

    // // Dynamically create the ItemWriter (e.g., for writing to a database)
    // public ItemWriter<Object> dynamicItemWriter() {
    //     System.out.println("Dynamic Writer is called");
    //     return customItemWriter;
    // }

}
