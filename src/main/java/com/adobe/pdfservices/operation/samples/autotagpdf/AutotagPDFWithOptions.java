/*
 * Copyright 2022 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it.If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.autotagpdf;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.AutotagPDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.autotag.AutotagOutputFiles;
import com.adobe.pdfservices.operation.pdfops.options.autotag.AutotagPDFOptions;

/**
 * This sample illustrates how to generate a tagged PDF along with a report and shift the headings in the
 * output PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class AutotagPDFWithOptions {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AutotagPDFWithOptions.class);

    public static void main(String[] args) {

        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            AutotagPDFOperation autotagPDFOperation = AutotagPDFOperation.createNew();

            // Provide an input FileRef for the operation
            autotagPDFOperation.setInput(FileRef.createFromLocalFile("src/main/resources/autotagPdfInput.pdf"));

            // Build AutotagPDF options and set them into the operation
            AutotagPDFOptions autotagPDFOptions = AutotagPDFOptions.autotagPDFOptionsBuilder()
                    .shiftHeadings()
                    .generateReport()
                    .build();
            autotagPDFOperation.setOptions(autotagPDFOptions);

            // Execute the operation
            AutotagOutputFiles autotagOutputFiles = autotagPDFOperation.execute(executionContext);

            // Save the output files at the specified location
            autotagOutputFiles.saveTaggedPDF("output/AutotagPDFWithOptions/autotagPdfInput-tagged.pdf");
            autotagOutputFiles.saveReport("output/AutotagPDFWithOptions/autotagPdfInput-report.xlsx");


        } catch (ServiceApiException | IOException | ServiceUsageException e) {
            System.out.println(e);
        }
    }
}
