/*
 * Copyright 2021 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.compresspdf;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CompressPDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.compresspdf.CompressPDFOptions;
import com.adobe.pdfservices.operation.pdfops.options.compresspdf.CompressionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to compress PDF by reducing the size of the PDF file on the basis of
 * provided compression level.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CompressPDFWithOptions {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressPDF.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            // Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CompressPDFOperation compressPDFOperation = CompressPDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/compressPDFInput.pdf");
            compressPDFOperation.setInput(source);

            // Build CompressPDF options from supported compression levels and set them into the operation
            CompressPDFOptions compressPDFOptions = CompressPDFOptions.compressPDFOptionsBuilder()
                    .withCompressionLevel(CompressionLevel.LOW)
                    .build();
            compressPDFOperation.setOptions(compressPDFOptions);

            // Execute the operation
            FileRef result = compressPDFOperation.execute(executionContext);

            // Save the result at the specified location
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
        return("output/CompressPDFWithOptions/compress" + timeStamp + ".pdf");
    }

}
