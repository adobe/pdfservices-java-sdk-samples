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

package com.adobe.pdfservices.operation.samples.rotatepages;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.RotatePagesOperation;
import com.adobe.pdfservices.operation.pdfops.options.PageRanges;
import com.adobe.pdfservices.operation.pdfops.options.rotatepages.Angle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample illustrates how to rotate pages in a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class RotatePDFPages {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(RotatePDFPages.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            RotatePagesOperation rotatePagesOperation = RotatePagesOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/rotatePagesInput.pdf");
            rotatePagesOperation.setInput(source);

            // Sets angle by 90 degrees (in clockwise direction) for rotating the specified pages of
            // the input PDF file.
            PageRanges firstPageRange = getFirstPageRangeForRotation();
            rotatePagesOperation.setAngleToRotatePagesBy(Angle._90, firstPageRange);

            // Sets angle by 180 degrees (in clockwise direction) for rotating the specified pages of
            // the input PDF file.
            PageRanges secondPageRange = getSecondPageRangeForRotation();
            rotatePagesOperation.setAngleToRotatePagesBy(Angle._180, secondPageRange);

            // Execute the operation.
            FileRef result = rotatePagesOperation.execute(executionContext);

            // Save the result to the specified location.
            result.saveAs("output/rotatePagesOutput.pdf");

        } catch (IOException | ServiceApiException | SdkException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static PageRanges getFirstPageRangeForRotation() {
        // Specify pages for rotation.
        PageRanges firstPageRange = new PageRanges();
        // Add page 1.
        firstPageRange.addSinglePage(1);

        // Add pages 3 to 4.
        firstPageRange.addRange(3, 4);
        return firstPageRange;
    }

    private static PageRanges getSecondPageRangeForRotation() {
        // Specify pages for rotation.
        PageRanges secondPageRange = new PageRanges();
        // Add page 2.
        secondPageRange.addSinglePage(2);

        return secondPageRange;
    }
}
