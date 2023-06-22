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

package com.adobe.pdfservices.operation.samples.deletepages;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.DeletePagesOperation;
import com.adobe.pdfservices.operation.pdfops.options.PageRanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to delete pages in a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class DeletePDFPages {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePDFPages.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            DeletePagesOperation deletePagesOperation = DeletePagesOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/deletePagesInput.pdf");
            deletePagesOperation.setInput(source);

            // Delete pages of the document (as specified by PageRanges).
            PageRanges pageRangeForDeletion = getPageRangeForDeletion();
            deletePagesOperation.setPageRanges(pageRangeForDeletion);

            // Execute the operation.
            FileRef result = deletePagesOperation.execute(executionContext);

            // Save the result to the specified location.
            String outputFilePath = createOutputFilePath();
            result.saveAs(outputFilePath);

        } catch (IOException | ServiceApiException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static PageRanges getPageRangeForDeletion() {
        // Specify pages for deletion.
        PageRanges pageRangeForDeletion = new PageRanges();
        // Add page 1.
        pageRangeForDeletion.addSinglePage(1);

        // Add pages 3 to 4.
        pageRangeForDeletion.addRange(3, 4);
        return pageRangeForDeletion;
    }

    //Generates a string containing a directory structure and file name for the output file.
    public static String createOutputFilePath(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return("output/DeletePDFPages/delete" + timeStamp + ".pdf");
    }
}
