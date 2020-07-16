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

package com.adobe.platform.operation.samples.protectpdf;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.ProtectPDFOperation;
import com.adobe.platform.operation.pdfops.options.protectpdf.EncryptionAlgorithm;
import com.adobe.platform.operation.pdfops.options.protectpdf.ProtectPDFOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample illustrates how to convert a PDF file into a password protected PDF file.
 * The password is used for encrypting PDF contents and will be required for viewing the PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ProtectPDF {
    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtectPDF.class);

    public static void main(String[] args) {

        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            // Build ProtectPDF options by setting a User Password and Encryption
            // Algorithm (used for encrypting the PDF file).
            ProtectPDFOptions protectPDFOptions = ProtectPDFOptions.passwordProtectOptionsBuilder()
                    .setUserPassword("encryptPassword")
                    .setEncryptionAlgorithm(EncryptionAlgorithm.AES_256)
                    .build();

            // Create a new operation instance.
            ProtectPDFOperation protectPDFOperation = ProtectPDFOperation.createNew(protectPDFOptions);

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/protectPDFInput.pdf");
            protectPDFOperation.setInput(source);

            // Execute the operation
            FileRef result = protectPDFOperation.execute(executionContext);

            // Save the result at the specified location
            result.saveAs("output/protectPDFOutput.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
