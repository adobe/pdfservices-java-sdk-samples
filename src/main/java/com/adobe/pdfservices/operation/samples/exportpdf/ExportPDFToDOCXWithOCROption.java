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
package com.adobe.pdfservices.operation.samples.exportpdf;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.ExportPDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.exportpdf.ExportOCRLocale;
import com.adobe.pdfservices.operation.pdfops.options.exportpdf.ExportPDFOptions;
import com.adobe.pdfservices.operation.pdfops.options.exportpdf.ExportPDFTargetFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to export a PDF file to a Word (DOCX) file. The OCR processing is also performed on the input PDF file to extract text from images in the document.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ExportPDFToDOCXWithOCROption {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPDFToDOCXWithOCROption.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();
            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            ExportPDFOperation exportPDFOperation = ExportPDFOperation.createNew(ExportPDFTargetFormat.DOCX);

            // Set operation input from a source file.
            FileRef sourceFileRef = FileRef.createFromLocalFile("src/main/resources/exportPDFInput.pdf");
            exportPDFOperation.setInput(sourceFileRef);

            //Create a new ExportPDFOptions instance from the specified OCR locale and set it into the operation.
            ExportPDFOptions exportPDFOptions = new ExportPDFOptions(ExportOCRLocale.EN_US);
            exportPDFOperation.setOptions(exportPDFOptions);

            // Execute the operation.
            FileRef result = exportPDFOperation.execute(executionContext);

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
        return("output/ExportPDFToDOCXWithOCROption/export" + timeStamp + ".docx");
    }
}
