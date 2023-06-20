/*
 * Copyright 2019 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.splitpdf;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.SplitPDFOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This sample illustrates how to split input PDF into multiple PDF files on the basis of the maximum number
 * of pages each of the output files can have.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class SplitPDFByNumberOfPages {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitPDFByNumberOfPages.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            SplitPDFOperation splitPDFOperation = SplitPDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/splitPDFInput.pdf");
            splitPDFOperation.setInput(source);

            // Set the maximum number of pages each of the output files can have.
            splitPDFOperation.setPageCount(2);

            // Execute the operation.
            List<FileRef> result = splitPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            String outputFilePath = createOutputFilePath();
            int index = 0;
            for (FileRef fileRef : result) {
                String saveOutputFilePath = String.format(outputFilePath, String.valueOf(index));
                fileRef.saveAs(saveOutputFilePath);
                index++;
            }

        } catch (IOException | ServiceApiException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    //Generates a string containing a directory structure and indexed file name for the output file.
    public static String createOutputFilePath(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return ("output/SplitPDFByNumberOfPages/split" + timeStamp + "_%s.pdf");
    }
}
