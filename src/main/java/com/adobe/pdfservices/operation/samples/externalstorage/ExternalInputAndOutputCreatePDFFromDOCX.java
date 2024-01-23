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

package com.adobe.pdfservices.operation.samples.externalstorage;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesJobStatus;
import com.adobe.pdfservices.operation.PDFServicesJobStatusResponse;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.exception.SDKException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.io.ExternalAsset;
import com.adobe.pdfservices.operation.io.ExternalStorageType;
import com.adobe.pdfservices.operation.pdfjobs.jobs.CreatePDFJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * This sample illustrates how to use external storage as input and output in PDF Services.
 * For this illustration a PDF file will be created and stored externally from a DOCX file stored externally.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ExternalInputAndOutputCreatePDFFromDOCX {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalInputAndOutputCreatePDFFromDOCX.class);

    public static void main(String[] args) {

        try {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creating external assets from pre signed URLs.
            String inputPreSignedURL = "INPUT_PRESIGNED_URL";
            String outputPreSignedURL = "OUTPUT_PRESIGNED_URL";
            Asset inputExternalAsset = new ExternalAsset(inputPreSignedURL, ExternalStorageType.S3);
            Asset outputExternalAsset = new ExternalAsset(outputPreSignedURL, ExternalStorageType.S3);

            // Creates a new job instance
            CreatePDFJob createPDFJob = new CreatePDFJob(inputExternalAsset).setOutput(outputExternalAsset);

            // Submit the job and gets the job result
            String location = pdfServices.submit(createPDFJob);

            // Poll to check job status and wait until job is done
            PDFServicesJobStatusResponse pdfServicesJobStatusResponse = null;
            while (pdfServicesJobStatusResponse == null || PDFServicesJobStatus.IN_PROGRESS.getValue().equals(pdfServicesJobStatusResponse.getStatus())) {
                pdfServicesJobStatusResponse = pdfServices.getJobStatus(location);
                // get retry interval from response
                Integer retryAfter = pdfServicesJobStatusResponse.getRetryInterval();
                TimeUnit.SECONDS.sleep(retryAfter);
            }

            LOGGER.info("Output is now available on the provided output external storage.");
        } catch (ServiceApiException | SDKException | ServiceUsageException | InterruptedException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
