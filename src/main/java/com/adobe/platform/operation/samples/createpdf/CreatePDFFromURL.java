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
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ClientContext;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;
import com.adobe.platform.operation.pdfops.options.createpdf.CreatePDFOptions;
import com.adobe.platform.operation.pdfops.options.createpdf.PageLayout;

/**
 * This sample illustrates how to create a PDF file from a web page URL along with relevant conversion options.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CreatePDFFromURL {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFFromURL.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create a default ClientContext and a new operation instance.
            ClientContext clientContext = ClientContext.createDefault();
            CreatePDFOperation htmlToPDFOperation = CreatePDFOperation.createNew();

            // Set operation input from a URL.
            FileRef source = FileRef.createFromURL(new URL("https://www.adobe.io"), CreatePDFOperation.SupportedSourceFormat.HTML.getMediaType());
            htmlToPDFOperation.setInput(source);

            // Provide any custom configuration options for the operation.
            setCustomOptions(htmlToPDFOperation);

            // Execute the operation.
            FileRef result = htmlToPDFOperation.execute(clientContext);

            // Save the result to the specified location.
            result.saveAs("output/createPdfFromUrlOutput.pdf");

        } catch (ServiceApiException | SdkException | IOException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    /**
     * Sets any custom options for the operation.
     *
     * @param htmlToPDFOperation operation instance for which the options are provided.
     */
    private static void setCustomOptions(CreatePDFOperation htmlToPDFOperation) {
        // Define the page layout, in this case an 11.5 x 8 inch page (effectively landscape orientation).
        PageLayout pageLayout = new PageLayout();
        pageLayout.setPageSize(11.5, 8);
        // Set the desired HTML-to-PDF conversion options.
        CreatePDFOptions htmlToPdfOptions = CreatePDFOptions.htmlOptionsBuilder()
                .includeHeaderFooter(false).withPageLayout(pageLayout).build();
        htmlToPDFOperation.setOptions(htmlToPdfOptions);
    }

}
