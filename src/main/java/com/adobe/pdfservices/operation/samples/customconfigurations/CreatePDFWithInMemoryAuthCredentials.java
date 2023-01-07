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

package com.adobe.pdfservices.operation.samples.customconfigurations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CreatePDFOperation;

/**
 * This sample illustrates how to provide in-memory auth credentials for performing an operation. This enables the
 * clients to fetch the credentials from a secret server during runtime, instead of storing them in a file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */

public class CreatePDFWithInMemoryAuthCredentials {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFWithInMemoryAuthCredentials.class);

    public static void main(String[] args) {

        try {

            /*
            Initial setup, create credentials instance.
            Replace the values of CLIENT_ID, CLIENT_SECRET, ORGANIZATION_ID and ACCOUNT_ID with their corresponding values
            present in the pdfservices-api-credentials.json file and PRIVATE_KEY_FILE_CONTENTS with contents of private.key file
            within the zip file which must have been downloaded at the end of Getting the Credentials workflow.
            */
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .withClientId("CLIENT_ID")
                    .withClientSecret("CLIENT_SECRET")
                    .withPrivateKey("PRIVATE_KEY_FILE_CONTENTS")
                    .withOrganizationId("ORGANIZATION_ID")
                    .withAccountId("ACCOUNT_ID")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CreatePDFOperation createPDFOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFInput.docx");
            createPDFOperation.setInput(source);

            // Execute the operation.
            FileRef result = createPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            String outputFilePath = createOutputFilePath();
            result.saveAs(outputFilePath);

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    //Generates a string containing a directory structure and file name for the output file.
    public static String createOutputFilePath(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return("output/CreatePDFWithInMemCredentials/create" + timeStamp + ".pdf");
    }
}
