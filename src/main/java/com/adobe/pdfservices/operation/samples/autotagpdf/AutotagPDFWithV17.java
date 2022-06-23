/*
 * Copyright 2021 Adobe
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
import com.adobe.pdfservices.operation.pdfops.options.autotagpdf.AutotagOutputFiles;
import com.adobe.pdfservices.operation.pdfops.options.autotagpdf.AutotagPDFOptions;
import com.adobe.pdfservices.operation.pdfops.options.autotagpdf.PDFVersion;

/**
 * This sample illustrates how to generate a tagged PDF with version 1.7 from PDF.
 * <p>
 * Refer to README.md for instructions on how to run the samples & understand output tagged pdf with pdf version 1.7.
 */

public class AutotagPDFWithV17 {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AutotagPDFWithV17.class);

    public static void main(String[] args) {

        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            AutotagPDFOperation autotagPDFOperation = AutotagPDFOperation.createNew();

            // Provide an input FileRef for the operation and take the name of the input file to be used in output file
            String filename = "src/main/resources/autotagPdfInput.pdf";
            FileRef source = FileRef.createFromLocalFile(filename);
            autotagPDFOperation.setInputFile(source);

            //Extract the name of the input file to be used in saving output file with same name
            String inputFileName = filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf('.'));

            // Build AutotagPDF options and set them into the operation
            AutotagPDFOptions autotagPDFOptions = AutotagPDFOptions.autotagPDFOptionsBuilder()
                    .pdfVersion(PDFVersion.v17)
                    .build();
            autotagPDFOperation.setOptions(autotagPDFOptions);

            // Execute the operation
            AutotagOutputFiles autotagOutputFiles = autotagPDFOperation.execute(executionContext);

            // Save the output files at the specified location
            String outputDirectory = "output/AutotagPDFWithV17/";
            String taggedPDFPath = outputDirectory  + inputFileName + "-taggedPDF.pdf";

            autotagOutputFiles.saveTaggedPDF(taggedPDFPath);

        } catch (ServiceApiException | IOException | ServiceUsageException e) {
            System.out.println(e);
        }
    }
}
