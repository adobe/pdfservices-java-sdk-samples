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

package com.adobe.platform.operation.samples.insertpages;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.InsertPagesOperation;
import com.adobe.platform.operation.pdfops.options.PageRanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample illustrates how to insert specific pages of multiple PDF files into a single PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class InsertPDFPages {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertPDFPages.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            InsertPagesOperation insertPagesOperation = InsertPagesOperation.createNew();

            // Set operation base input from a source file.
            FileRef baseSourceFile = FileRef.createFromLocalFile("src/main/resources/baseInput.pdf");
            insertPagesOperation.setBaseInput(baseSourceFile);

            // Create a FileRef instance using a local file.
            FileRef firstFileToInsert = FileRef.createFromLocalFile("src/main/resources/firstFileToInsertInput.pdf");
            PageRanges pageRanges = getPageRangeForFirstFile();

            // Adds the pages (specified by the page ranges) of the input PDF file to be inserted at
            // the specified page of the base PDF file.
            insertPagesOperation.addPagesToInsertAt(firstFileToInsert, pageRanges, 2);

            // Create a FileRef instance using a local file.
            FileRef secondFileToInsert = FileRef.createFromLocalFile("src/main/resources/secondFileToInsertInput.pdf");

            // Adds all the pages of the input PDF file to be inserted at the specified page of the
            // base PDF file.
            insertPagesOperation.addPagesToInsertAt(secondFileToInsert, 3);

            // Execute the operation.
            FileRef result = insertPagesOperation.execute(executionContext);

            // Save the result to the specified location.
            result.saveAs("output/insertPagesOutput.pdf");

        } catch (IOException | ServiceApiException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static PageRanges getPageRangeForFirstFile() {
        // Specify which pages of the first file are to be inserted in the base file.
        PageRanges pageRanges = new PageRanges();
        // Add pages 1 to 3.
        pageRanges.addRange(1, 3);

        // Add page 4.
        pageRanges.addSinglePage(4);

        return pageRanges;
    }

}
