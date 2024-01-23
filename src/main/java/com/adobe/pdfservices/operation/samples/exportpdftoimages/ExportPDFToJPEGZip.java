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

package com.adobe.pdfservices.operation.samples.exportpdftoimages;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.ExportPDFToImagesJob;
import com.adobe.pdfservices.operation.pdfjobs.params.exportpdftoimages.ExportPDFToImagesOutputType;
import com.adobe.pdfservices.operation.pdfjobs.params.exportpdftoimages.ExportPDFToImagesParams;
import com.adobe.pdfservices.operation.pdfjobs.params.exportpdftoimages.ExportPDFToImagesTargetFormat;
import com.adobe.pdfservices.operation.pdfjobs.result.ExportPDFToImagesResult;
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
import java.util.List;

/**
 * This sample illustrates how to export a PDF file to JPEG.
 * <p>
 * The resulting file is a ZIP archive containing one image per page of the source PDF file
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ExportPDFToJPEGZip {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPDFToJPEGZip.class);

    public static void main(String[] args) {
        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/exportPDFToImagesInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Create parameters for the job
            ExportPDFToImagesParams exportPDFToImagesParams = ExportPDFToImagesParams.exportPDFToImagesParamsBuilder(ExportPDFToImagesTargetFormat.JPEG, ExportPDFToImagesOutputType.ZIP_OF_PAGE_IMAGES)
                    .build();

            // Creates a new job instance
            ExportPDFToImagesJob exportPDFToImagesJob = new ExportPDFToImagesJob(asset, exportPDFToImagesParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(exportPDFToImagesJob);
            PDFServicesResponse<ExportPDFToImagesResult> pdfServicesResponse = pdfServices.getJobResult(location, ExportPDFToImagesResult.class);

            // Get content from the resulting asset(s)
            List<Asset> resultAssets = pdfServicesResponse.getResult().getAssets();
            StreamAsset streamAsset = pdfServices.getContent(resultAssets.get(0));

            LOGGER.info("Media type of the received asset is " + streamAsset.getMimeType());

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
        Files.createDirectories(Paths.get("output/ExportPDFToJPEGZip"));
        return ("output/ExportPDFToJPEGZip/export" + timeStamp + ".zip");
    }
}
