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

package com.adobe.pdfservices.operation.samples.createpdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CreatePDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.createpdf.CreatePDFOptions;
import com.adobe.pdfservices.operation.pdfops.options.createpdf.PageLayout;

/**
 * This sample illustrates how to provide data inputs to an HTML file before converting it to PDF. The data input is used
 * by the javascript in the HTML file to manipulate the HTML DOM, thus effectively updating the source HTML file.
 * This mechanismn can be used to provide data to the template HTML dynamically and convert it into a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CreatePDFFromDynamicHTML {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFFromDynamicHTML.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            CreatePDFOperation htmlToPDFOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFFromDynamicHtmlInput.zip");
            htmlToPDFOperation.setInput(source);

            // Provide any custom configuration options for the operation.
            setCustomOptions(htmlToPDFOperation);

            // Execute the operation.
            FileRef result = htmlToPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            String outputFilePath = createOutputFilePath();
            result.saveAs(outputFilePath);

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    /**
     * Sets any custom options for the operation.
     *
     * @param htmlToPDFOperation operation instance for which the options are provided.
     */
    private static void setCustomOptions(CreatePDFOperation htmlToPDFOperation) {
        // Define the page layout, in this case an 8 x 11.5 inch page (effectively portrait orientation).
        PageLayout pageLayout = new PageLayout();
        pageLayout.setPageSize(8, 11.5);

        //Set the dataToMerge field that needs to be populated in the HTML before its conversion
        JSONObject dataToMerge = new JSONObject();
        dataToMerge.put("title","Create, Convert PDFs and More!");
        dataToMerge.put("sub_title","Easily integrate PDF actions within your document workflows.");

        // Set the desired HTML-to-PDF conversion options.
        CreatePDFOptions htmlToPdfOptions = CreatePDFOptions.htmlOptionsBuilder()
                .includeHeaderFooter(true)
                .withPageLayout(pageLayout)
                .withDataToMerge(dataToMerge)
                .build();
        htmlToPDFOperation.setOptions(htmlToPdfOptions);
    }

    //Generates a string containing a directory structure and file name for the output file.
    public static String createOutputFilePath(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return("output/CreatePDFFromDynamicHTML/create" + timeStamp + ".pdf");
    }

}
