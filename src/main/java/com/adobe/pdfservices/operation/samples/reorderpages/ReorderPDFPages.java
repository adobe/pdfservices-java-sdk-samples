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

package com.adobe.pdfservices.operation.samples.reorderpages;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.ReorderPagesPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.PageRanges;
import com.adobe.pdfservices.operation.pdfjobs.params.reorderpages.ReorderPagesParams;
import com.adobe.pdfservices.operation.pdfjobs.result.ReorderPagesResult;
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
 * This sample illustrates how to reorder the pages in a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ReorderPDFPages {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ReorderPDFPages.class);

    public static void main(String[] args) {
        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/reorderPagesInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            PageRanges pagesToReorder = getPageRangeForReorder();

            // Create parameters for the job
            ReorderPagesParams reorderPagesParams = ReorderPagesParams.reorderPagesParamsBuilder(asset, pagesToReorder) //
                    // Add the asset as input to the params, along with its page order
                    .build();

            // Creates a new job instance
            ReorderPagesPDFJob reorderPagesPDFJob = new ReorderPagesPDFJob(reorderPagesParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(reorderPagesPDFJob);
            PDFServicesResponse<ReorderPagesResult> pdfServicesResponse = pdfServices.getJobResult(location, ReorderPagesResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getAsset();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);

            // Creates an output stream and copy stream asset's content to it
            String outputFilePath = createOutputFilePath();
            LOGGER.info(String.format("Saving asset at %s", outputFilePath));

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            outputStream.close();
        } catch (IOException | ServiceApiException | SDKException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static PageRanges getPageRangeForReorder() {
        // Specify order of the pages for an output document
        PageRanges pageRanges = new PageRanges();
        // Add pages 3 to 4
        pageRanges.addRange(3, 4);

        // Add page 1
        pageRanges.addSinglePage(1);

        return pageRanges;
    }

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/ReorderPDFPages"));
        return ("output/ReorderPDFPages/reorder" + timeStamp + ".pdf");
    }
}
