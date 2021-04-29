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
import com.adobe.pdfservices.operation.pdfops.options.PageRanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * This sample illustrates how to split input PDF into multiple PDF files on the basis of page ranges.
 * Each page range corresponds to a single output file having the pages specified in the page range.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class SplitPDFByPageRanges {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitPDFByPageRanges.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            SplitPDFOperation splitPDFOperation = SplitPDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/splitPDFInput.pdf");
            splitPDFOperation.setInput(source);

            // Set the page ranges where each page range corresponds to a single output file.
            PageRanges pageRanges = getPageRanges();
            splitPDFOperation.setPageRanges(pageRanges);

            // Execute the operation.
            List<FileRef> result = splitPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            int index = 0;
            for (FileRef fileRef : result) {
                fileRef.saveAs("output/SplitPDFByPageRangesOutput_" + index + ".pdf");
                index++;
            }

        } catch (IOException | ServiceApiException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static PageRanges getPageRanges() {
        // Specify page ranges.
        PageRanges pageRanges = new PageRanges();
        // Add page 1.
        pageRanges.addSinglePage(1);

        // Add pages 3 to 4.
        pageRanges.addRange(3, 4);
        return pageRanges;
    }

}
