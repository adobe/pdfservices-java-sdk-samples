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

package com.adobe.platform.operation.samples.createpdf;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ClientContext;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;

/**
 * This sample illustrates how to create a PDF file from a PPTX file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CreatePDFFromPPTX {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFFromPPTX.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create a ClientContext using a config file, and a new operation instance.
            ClientContext clientContext = ClientContext.createFromFile("dc-services-sdk-config.json");
            CreatePDFOperation createPdfOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFInput.pptx");
            createPdfOperation.setInput(source);

            // Execute the operation.
            FileRef result = createPdfOperation.execute(clientContext);

            // Save the result to the specified location.
            result.saveAs("output/createPDFFromPPTX.pdf");

        } catch (ServiceApiException | IOException | SdkException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }

    }
}
