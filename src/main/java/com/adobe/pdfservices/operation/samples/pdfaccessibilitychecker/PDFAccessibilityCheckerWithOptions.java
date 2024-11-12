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

package com.adobe.pdfservices.operation.samples.pdfaccessibilitychecker;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.PDFAccessibilityCheckerJob;
import com.adobe.pdfservices.operation.pdfjobs.params.pdfaccessibilitychecker.PDFAccessibilityCheckerParams;
import com.adobe.pdfservices.operation.pdfjobs.result.PDFAccessibilityCheckerResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to run accessibility Checker on input PDF file for given page start and page end
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class PDFAccessibilityCheckerWithOptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFAccessibilityCheckerWithOptions.class);

    public static void main(String[] args) {

        try (
            InputStream inputStream = Files
                        .newInputStream(new File("src/main/resources/accessibilityCheckerInput.pdf")
                                .toPath())) {

            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"),
                    System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Creates parameters for the job
            PDFAccessibilityCheckerParams pdfAccessibilityCheckerParams = PDFAccessibilityCheckerParams
                    .pdfAccessibilityCheckerParamsBuilder().withPageStart(1).withPageEnd(2).build();

            // Creates a new job instance
            PDFAccessibilityCheckerJob pdfAccessibilityCheckerJob = new PDFAccessibilityCheckerJob(asset)
                    .setParams(pdfAccessibilityCheckerParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(pdfAccessibilityCheckerJob);
            PDFServicesResponse<PDFAccessibilityCheckerResult> pdfServicesResponse = pdfServices
                    .getJobResult(location, PDFAccessibilityCheckerResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            Asset report = pdfServicesResponse.getResult().getReport();
            StreamAsset streamAssetReport = pdfServices.getContent(report);

            // Creates output streams and copy stream asset's content to it
            String outputFilePath = createOutputFilePath();
            String outputFilePathReport = createOutputFilePathForReport();

            LOGGER.info(String.format("Saving asset at %s", outputFilePath));
            LOGGER.info(String.format("Saving report at %s", outputFilePathReport));

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            OutputStream outputStreamReport = Files.newOutputStream(new File(outputFilePathReport).toPath());

            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            IOUtils.copy(streamAssetReport.getInputStream(), outputStreamReport);

            outputStream.close();
            outputStreamReport.close();
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException ex) {
            System.out.println("Exception encountered while executing operation: "+ ex);
        }
    }

    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/PDFAccessibilityCheckerWithOptions"));
        return ("output/PDFAccessibilityCheckerWithOptions/accessibility" + timeStamp + ".pdf");
    }

    public static String createOutputFilePathForReport() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/PDFAccessibilityCheckerWithOptions"));
        return ("output/PDFAccessibilityCheckerWithOptions/accessibility" + timeStamp + ".json");
    }
}
