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

package com.adobe.pdfservices.operation.samples.customconfigurations;

import com.adobe.pdfservices.operation.PDFServices;
import com.adobe.pdfservices.operation.PDFServicesMediaType;
import com.adobe.pdfservices.operation.PDFServicesResponse;
import com.adobe.pdfservices.operation.Region;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.auth.ServicePrincipalCredentials;
import com.adobe.pdfservices.operation.config.ClientConfig;
import com.adobe.pdfservices.operation.exception.SDKException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.Asset;
import com.adobe.pdfservices.operation.io.StreamAsset;
import com.adobe.pdfservices.operation.pdfjobs.jobs.ExportPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.exportpdf.ExportPDFParams;
import com.adobe.pdfservices.operation.pdfjobs.params.exportpdf.ExportPDFTargetFormat;
import com.adobe.pdfservices.operation.pdfjobs.result.ExportPDFResult;
import org.apache.commons.io.IOUtils;
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
 * This sample illustrates how to specify the region for the SDK. This enables the client to configure the SDK to
 * process the documents in the specified region.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ExportPDFWithSpecifiedRegion {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPDFWithSpecifiedRegion.class);

    public static void main(String[] args) {

        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/exportPDFInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates client config instance with the specified region.
            ClientConfig clientConfig = ClientConfig.builder()
                    .setRegion(Region.EU)
                    .build();

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials, clientConfig);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Create parameters for the job
            ExportPDFParams exportPDFParams = ExportPDFParams.exportPDFParamsBuilder(ExportPDFTargetFormat.DOCX)
                    .build();

            // Creates a new job instance
            ExportPDFJob exportPDFJob = new ExportPDFJob(asset, exportPDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(exportPDFJob);
            PDFServicesResponse<ExportPDFResult> pdfServicesResponse = pdfServices.getJobResult(location, ExportPDFResult.class);

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

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/ExportPDFWithSpecifiedRegion"));
        return ("output/ExportPDFWithSpecifiedRegion/export" + timeStamp + ".docx");
    }
}
