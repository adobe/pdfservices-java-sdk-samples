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

package com.adobe.platform.operation.samples.deletepages;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.DeletePagesOperation;
import com.adobe.platform.operation.pdfops.options.PageRanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
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
            result.saveAs("output/deletePagesOutput.pdf");

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
}
