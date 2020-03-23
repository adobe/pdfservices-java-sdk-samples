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

package com.adobe.platform.operation.samples.combine;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CombineFilesOperation;
import com.adobe.platform.operation.pdfops.options.PageRanges;

/**
 * This sample illustrates how to combine specific pages of multiple PDF files into a single PDF file.
 * <p>
 * Note that the SDK supports combining upto 12 files in one operation
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CombinePDFWithPageRanges {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CombinePDFWithPageRanges.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("dc-services-sdk-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CombineFilesOperation combineFilesOperation = CombineFilesOperation.createNew();

            // Create a FileRef instance from a local file.
            FileRef firstFileToCombine = FileRef.createFromLocalFile("src/main/resources/combineFileWithPageRangeInput1.pdf");
            PageRanges pageRangesForFirstFile = getPageRangeForFirstFile();
            // Add the first file as input to the operation, along with its page range.
            combineFilesOperation.addInput(firstFileToCombine, pageRangesForFirstFile);

            // Create a second FileRef instance using a local file.
            FileRef secondFileToCombine = FileRef.createFromLocalFile("src/main/resources/combineFileWithPageRangeInput2.pdf");
            PageRanges pageRangesForSecondFile = getPageRangeForSecondFile();
            // Add the second file as input to the operation, along with its page range.
            combineFilesOperation.addInput(secondFileToCombine, pageRangesForSecondFile);

            // Execute the operation.
            FileRef result = combineFilesOperation.execute(executionContext);

            // Save the result to the specified location.
            result.saveAs("output/combineFilesWithPageOptionsOutput.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }

    }

    private static PageRanges getPageRangeForSecondFile() {
        // Specify which pages of the second file are to be included in the combined file.
        PageRanges pageRangesForSecondFile = new PageRanges();
        // Add all pages including and after page 3.
        pageRangesForSecondFile.addAllFrom(3);
        return pageRangesForSecondFile;
    }

    private static PageRanges getPageRangeForFirstFile() {
        // Specify which pages of the first file are to be included in the combined file.
        PageRanges pageRangesForFirstFile = new PageRanges();
        // Add page 1.
        pageRangesForFirstFile.addSinglePage(1);
        // Add page 2.
        pageRangesForFirstFile.addSinglePage(2);
        // Add pages 3 to 4.
        pageRangesForFirstFile.addRange(3, 4);
        return pageRangesForFirstFile;
    }


}
