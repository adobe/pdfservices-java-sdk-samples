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

import com.adobe.platform.operation.Authentication;
import com.adobe.platform.operation.ClientContext;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;

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

            // Initial setup, create a ClientContext using a config file.
            ClientContext clientContext = ClientContext.createFromFile("dc-services-sdk-config.json");
            /*
            Set this variable to the value of "identity" key in dc-services-sdk-config.json that you received in Adobe
            Document Cloud Services SDK welcome email.
            */
            String authenticationJsonString = "";

            // Create a new ClientContext instance with the provided authentication credentials.
            Authentication authentication = Authentication.create(authenticationJsonString);
            ClientContext contextWithAuth = clientContext.withAuthentication(authentication);

            // Create a new Operation instance.
            CreatePDFOperation createPDFOperation = CreatePDFOperation.createNew();
            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFInput.docx");
            createPDFOperation.setInput(source);

            // Execute the operation using the ClientContext created with the provided Authentication credentials.
            FileRef result = createPDFOperation.execute(contextWithAuth);

            // Save the result to the specified location.
            result.saveAs("output/createPDFWithInMemCredentials.pdf");

        } catch (ServiceApiException | IOException | SdkException ex) {
            LOGGER.warn("Please note that the variable authenticationJsonString needs to be initialized with the value " +
                    "of \"identity\" key in dc-services-sdk-config.json file.");
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
