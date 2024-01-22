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

package com.adobe.pdfservices.operation.samples.insertpages;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.InsertPagesPDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.PageRanges;
import com.adobe.pdfservices.operation.pdfjobs.params.insertpages.InsertPagesParams;
import com.adobe.pdfservices.operation.pdfjobs.result.InsertPagesResult;
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
 * This sample illustrates how to insert specific pages of multiple PDF files into a single PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class InsertPDFPages {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertPDFPages.class);

    public static void main(String[] args) {
        try (
                InputStream baseInputStream = Files.newInputStream(new File("src/main/resources/baseInput.pdf").toPath());
                InputStream firstInputStreamToInsert = Files.newInputStream(new File("src/main/resources/firstFileToInsertInput.pdf").toPath());
                InputStream secondInputStreamToInsert = Files.newInputStream(new File("src/main/resources/secondFileToInsertInput.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset baseAsset = pdfServices.upload(baseInputStream, PDFServicesMediaType.PDF.getMediaType());
            Asset firstAssetToInsert = pdfServices.upload(firstInputStreamToInsert, PDFServicesMediaType.PDF.getMediaType());
            Asset secondAssetToInsert = pdfServices.upload(secondInputStreamToInsert, PDFServicesMediaType.PDF.getMediaType());

            PageRanges pageRanges = getPageRangeForFirstFile();

            // Create parameters for the job
            InsertPagesParams insertPagesParams = InsertPagesParams.insertPagesParamsBuilder(baseAsset).addPagesToInsertAt(firstAssetToInsert, pageRanges, 2) // Add the first asset as input to the
                    // params, along with its page ranges and base page
                    .addPagesToInsertAt(secondAssetToInsert, 3) // Add the second asset as input to the params, along
                    // with base page
                    .build();

            // Creates a new job instance
            InsertPagesPDFJob insertPagesJob = new InsertPagesPDFJob(insertPagesParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(insertPagesJob);
            PDFServicesResponse<InsertPagesResult> pdfServicesResponse = pdfServices.getJobResult(location, InsertPagesResult.class);

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

    private static PageRanges getPageRangeForFirstFile() {
        // Specify which pages of the first file are to be inserted in the base file
        PageRanges pageRanges = new PageRanges();
        // Add pages 1 to 3
        pageRanges.addRange(1, 3);

        // Add page 4
        pageRanges.addSinglePage(4);

        return pageRanges;
    }

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/InsertPDFPages"));
        return ("output/InsertPDFPages/insert" + timeStamp + ".pdf");
    }

}
