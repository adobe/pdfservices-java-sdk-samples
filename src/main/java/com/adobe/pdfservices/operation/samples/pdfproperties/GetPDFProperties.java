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

package com.adobe.pdfservices.operation.samples.pdfproperties;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesMediaType;
import com.adobe.pdfservices.operation.PDFServicesResponse;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.exception.SDKException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.pdfjobs.jobs.PDFPropertiesJob;
import com.adobe.pdfservices.operation.pdfjobs.params.pdfproperties.PDFPropertiesParams;
import com.adobe.pdfservices.operation.pdfjobs.result.PDFPropertiesResult;
import com.adobe.pdfservices.operation.pdfjobs.result.pdfproperties.PDFProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * This sample illustrates how to retrieve properties of an input PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class GetPDFProperties {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPDFProperties.class);

    public static void main(String[] args) {

        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/pdfPropertiesInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Create parameters for the job
            PDFPropertiesParams pdfPropertiesParams = PDFPropertiesParams.pdfPropertiesParamsBuilder()
                    .includePageLevelProperties()
                    .build();

            // Creates a new job instance
            PDFPropertiesJob pdfPropertiesJob = new PDFPropertiesJob(asset).setParams(pdfPropertiesParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(pdfPropertiesJob);
            PDFServicesResponse<PDFPropertiesResult> pdfServicesResponse = pdfServices.getJobResult(location, PDFPropertiesResult.class);

            PDFProperties pdfProperties = pdfServicesResponse.getResult().getPdfProperties();

            // Fetch the requisite properties of the specified PDF.
            LOGGER.info("Size of the specified PDF file: {}", pdfProperties.getDocument().getFileSize());
            LOGGER.info("Version of the specified PDF file: {}", pdfProperties.getDocument().getPDFVersion());
            LOGGER.info("Page count of the specified PDF file: {}", pdfProperties.getDocument().getPageCount());
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
