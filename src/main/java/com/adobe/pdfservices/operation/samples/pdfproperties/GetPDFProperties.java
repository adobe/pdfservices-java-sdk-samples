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

package com.adobe.pdfservices.operation.samples.pdfproperties;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.io.pdfproperties.PDFProperties;
import com.adobe.pdfservices.operation.pdfops.PDFPropertiesOperation;
import com.adobe.pdfservices.operation.pdfops.options.pdfproperties.PDFPropertiesOptions;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample illustrates how to retrieve properties of an input PDF file.
 *
 * Refer to README.md for instructions on how to run the samples.
 */
public class GetPDFProperties {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPDFProperties.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            PDFPropertiesOperation pdfPropertiesOperation = PDFPropertiesOperation.createNew();

            // Provide an input FileRef for the operation
            FileRef source = FileRef.createFromLocalFile("src/main/resources/pdfPropertiesInput.pdf");
            pdfPropertiesOperation.setInputFile(source);

            // Build PDF Properties options to include page level properties and set them into the operation
            PDFPropertiesOptions pdfPropertiesOptions = PDFPropertiesOptions.PDFPropertiesOptionsBuilder()
                    .includePageLevelProperties(true)
                    .build();
            pdfPropertiesOperation.setOptions(pdfPropertiesOptions);

            // Execute the operation.
            PDFProperties pdfProperties = pdfPropertiesOperation.execute(executionContext);

            // Fetch the requisite properties of the specified PDF.
            LOGGER.info("Size of the specified PDF file: {}", pdfProperties.getDocument().getFileSize());
            LOGGER.info("Version of the specified PDF file: {}", pdfProperties.getDocument().getPDFVersion());
            LOGGER.info("Page count of the specified PDF file: {}", pdfProperties.getDocument().getPageCount());

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
