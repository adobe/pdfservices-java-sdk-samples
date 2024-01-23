/*
 * Copyright 2024 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.htmltopdf;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesMediaType;
import com.adobe.pdfservices.operation.PDFServicesResponse;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.exception.SDKException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.io.StreamAsset;
import com.adobe.pdfservices.operation.pdfjobs.jobs.HTMLToPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.htmltopdf.HTMLToPDFParams;
import com.adobe.pdfservices.operation.pdfjobs.params.htmltopdf.PageLayout;
import com.adobe.pdfservices.operation.pdfjobs.result.HTMLToPDFResult;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to provide data inputs to an HTML file before converting it to PDF. The data input is
 * used by the javascript in the HTML file to manipulate the HTML DOM, thus effectively updating the source HTML file.
 * This mechanismn can be used to provide data to the template HTML dynamically and convert it into a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class DynamicHTMLToPDF {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicHTMLToPDF.class);

    public static void main(String[] args) {

        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/createPDFFromDynamicHtmlInput.zip").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.ZIP.getMediaType());

            // Create parameters for the job
            HTMLToPDFParams htmlToPDFParams = getHTMLToPDFParams();

            // Creates a new job instance
            HTMLToPDFJob htmLtoPDFJob = new HTMLToPDFJob(asset).setParams(htmlToPDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(htmLtoPDFJob);
            PDFServicesResponse<HTMLToPDFResult> pdfServicesResponse = pdfServices.getJobResult(location, HTMLToPDFResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            // Creates an output stream and copy stream asset's content to it
            String outputFilePath = createOutputFilePath();
            LOGGER.info(String.format("Saving asset at %s", outputFilePath));

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            outputStream.close();
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    private static HTMLToPDFParams getHTMLToPDFParams() {
        // Define the page layout, in this case an 8 x 11.5 inch page (effectively portrait orientation)
        PageLayout pageLayout = new PageLayout();
        pageLayout.setPageSize(8, 11.5);

        // Sets the dataToMerge field that needs to be populated in the HTML before its conversion
        JSONObject dataToMerge = new JSONObject();
        dataToMerge.put("title", "Create, Convert PDFs and More!");
        dataToMerge.put("sub_title", "Easily integrate PDF actions within your document workflows.");

        return new HTMLToPDFParams.Builder()
                .includeHeaderFooter(true)
                .withPageLayout(pageLayout)
                .withDataToMerge(dataToMerge)
                .build();
    }

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/DynamicHTMLToPDF"));
        return ("output/DynamicHTMLToPDF/htmltopdf" + timeStamp + ".pdf");
    }

}
