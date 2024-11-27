package com.ugf360.dbdemo.controller;

import com.ugf360.dbdemo.springconfig.BatchConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

  public FileUploadController () {
    System.out.println("XXXXX FileUploadController instantiated");
  }


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BatchConfig batchConfig;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("fileType") String fileType,
                             @RequestParam("entityKey") String entityKey) throws Exception {

        // Create a temporary file to store the uploaded file
        String tempFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), tempFileName);
        Files.copy(file.getInputStream(), tempFilePath);
        System.out.println("XXXXX- Temp file directory " + tempFilePath);

        // Set up job parameters, including file path, file type, and entity key
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", tempFilePath.toString())
                .addString("fileType", fileType)  // Set file type (Excel, CSV, JSON)
                .addString("entityKey", entityKey)  // Set entity key (e.g., "U3Entity")
                .addString("sheetIndex","0")
                .toJobParameters();

        // Create dynamic job
        Job job = batchConfig.createDynamicJob(jobParameters);

        // Launch the job
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        return "Job started with execution ID: " + jobExecution.getId();
    }
}
