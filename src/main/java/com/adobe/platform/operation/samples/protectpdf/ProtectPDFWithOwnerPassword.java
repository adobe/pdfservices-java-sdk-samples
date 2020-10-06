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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.ProtectPDFOperation;
import com.adobe.platform.operation.pdfops.options.protectpdf.ContentEncryption;
import com.adobe.platform.operation.pdfops.options.protectpdf.EncryptionAlgorithm;
import com.adobe.platform.operation.pdfops.options.protectpdf.Permission;
import com.adobe.platform.operation.pdfops.options.protectpdf.Permissions;
import com.adobe.platform.operation.pdfops.options.protectpdf.ProtectPDFOptions;

/**
 * This sample illustrates how to secure a PDF file with owner password and allow certain access permissions 
 * such as copying and editing the contents, and printing of the document at low resolution.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ProtectPDFWithOwnerPassword {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtectPDFWithOwnerPassword.class);

    public static void main(String[] args) {

        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            // Create new permissions instance and add the required permissions
            Permissions permissions = Permissions.createNew();
            permissions.addPermission(Permission.PRINT_LOW_QUALITY);
            permissions.addPermission(Permission.EDIT_DOCUMENT_ASSEMBLY);
            permissions.addPermission(Permission.COPY_CONTENT);

            // Build ProtectPDF options by setting an Owner/Permissions Password, Permissions,
            // Encryption Algorithm (used for encrypting the PDF file) and specifying the type of content to encrypt.
            ProtectPDFOptions protectPDFOptions = ProtectPDFOptions.passwordProtectOptionsBuilder()
                    .setOwnerPassword("password")
                    .setPermissions(permissions)
                    .setEncryptionAlgorithm(EncryptionAlgorithm.AES_256)
                    .setContentEncryption(ContentEncryption.ALL_CONTENT_EXCEPT_METADATA)
                    .build();

            // Create a new operation instance.
            ProtectPDFOperation protectPDFOperation = ProtectPDFOperation.createNew(protectPDFOptions);

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/protectPDFInput.pdf");
            protectPDFOperation.setInput(source);

            // Execute the operation
            FileRef result = protectPDFOperation.execute(executionContext);

            // Save the result at the specified location
            result.saveAs("output/protectPDFWithOwnerPasswordOutput.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
