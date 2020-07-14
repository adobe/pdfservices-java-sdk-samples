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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ClientConfig;
import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;

/**
 * This sample illustrates how to create a PDF file from a DOCX file, and then save the result to an output stream.
 *
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CreatePDFFromDOCXToOutputStream {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFFromDOCXToOutputStream.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CreatePDFOperation createPdfOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFInput.docx");
            createPdfOperation.setInput(source);

            // Execute the operation.
            FileRef result = createPdfOperation.execute(executionContext);

            // Create an OutputStream and save the result to the stream.
            try (OutputStream outputStream = prepareOutputStream()) {
                result.saveAs(outputStream);
            }
        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    /**
     * Prepares an OutputStream over a predetermined result file.
     *
     * @return the OutputStream instance
     */
    private static FileOutputStream prepareOutputStream() throws FileNotFoundException {
        File file = new File("output/createPDFAsStream.pdf");

        // Create the result directories if they don't exist.
        file.getParentFile().mkdirs();

        return new FileOutputStream(file);
    }

}
