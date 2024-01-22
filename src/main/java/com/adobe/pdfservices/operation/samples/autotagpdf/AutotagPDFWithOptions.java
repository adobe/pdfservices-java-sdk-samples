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
package com.adobe.pdfservices.operation.samples.autotagpdf;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.AutotagPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.autotag.AutotagPDFParams;
import com.adobe.pdfservices.operation.pdfjobs.result.AutotagPDFResult;
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
 * This sample illustrates how to generate a tagged PDF along with a report and shift the headings in
 * the output PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class AutotagPDFWithOptions {
    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AutotagPDFWithOptions.class);

    public static void main(String[] args) {

        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/autotagPDFInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Create parameters for the job
            AutotagPDFParams autotagPDFParams = AutotagPDFParams.autotagPDFParamsBuilder()
                    .generateReport()
                    .shiftHeadings()
                    .build();

            // Creates a new job instance
            AutotagPDFJob autotagPDFJob = new AutotagPDFJob(asset).setParams(autotagPDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(autotagPDFJob);
            PDFServicesResponse<AutotagPDFResult> pdfServicesResponse = pdfServices.getJobResult(location, AutotagPDFResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getTaggedPDF();
            Asset resultAssetReport = pdfServicesResponse.getResult().getReport();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);
            StreamAsset streamAssetReport = pdfServices.getContent(resultAssetReport);

            // Creating output streams and copying stream assets' content to it
            String outputFilePath = createOutputFilePath();
            String outputFilePathReport = createOutputFilePathForTaggingReport();
            LOGGER.info(String.format("Saving asset at %s", outputFilePath));
            LOGGER.info(String.format("Saving asset at %s", outputFilePath));

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            OutputStream outputStreamReport = Files.newOutputStream(new File(outputFilePathReport).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            IOUtils.copy(streamAssetReport.getInputStream(), outputStreamReport);
            outputStream.close();
            outputStreamReport.close();
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    // Generates a string containing a directory structure and file name for the tagged PDF output
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/AutotagPDFWithOptions"));
        return ("output/AutotagPDFWithOptions/autotag-tagged" + timeStamp + ".pdf");
    }

    // Generates a string containing a directory structure and file name for the tagging report output
    public static String createOutputFilePathForTaggingReport() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/AutotagPDFWithOptions"));
        return ("output/AutotagPDFWithOptions/autotag-report" + timeStamp + ".xlsx");
    }
}
