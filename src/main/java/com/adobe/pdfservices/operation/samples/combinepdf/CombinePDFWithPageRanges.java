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

package com.adobe.pdfservices.operation.samples.combinepdf;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.CombinePDFJob;
import com.adobe.pdfservices.operation.pdfjobs.params.PageRanges;
import com.adobe.pdfservices.operation.pdfjobs.params.combinepdf.CombinePDFParams;
import com.adobe.pdfservices.operation.pdfjobs.result.CombinePDFResult;
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
import java.util.ArrayList;
import java.util.List;

/**
 * This sample illustrates how to combine specific pages of multiple PDF files into a single PDF file.
 * <p>
 * Note that the SDK supports combining upto 20 files in one operation
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CombinePDFWithPageRanges {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(CombinePDFWithPageRanges.class);

    public static void main(String[] args) {

        try (
                InputStream inputStream1 = Files.newInputStream(new File("src/main/resources/combineFileWithPageRangeInput1.pdf").toPath());
                InputStream inputStream2 = Files.newInputStream(new File("src/main/resources/combineFileWithPageRangeInput2.pdf").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            List<StreamAsset> streamAssets = new ArrayList<>();
            streamAssets.add(new StreamAsset(inputStream1, PDFServicesMediaType.PDF.getMediaType()));
            streamAssets.add(new StreamAsset(inputStream2, PDFServicesMediaType.PDF.getMediaType()));
            List<Asset> assets = pdfServices.uploadAssets(streamAssets);

            PageRanges pageRangesForFirstFile = getPageRangeForFirstFile();
            PageRanges pageRangesForSecondFile = getPageRangeForSecondFile();

            // Create parameters for the job
            CombinePDFParams combinePDFParams = CombinePDFParams.combinePDFParamsBuilder().addAsset(assets.get(0), pageRangesForFirstFile) // Add the first asset as input to the params,
                    // along with its page ranges
                    .addAsset(assets.get(1), pageRangesForSecondFile) // Add the second asset as input to the params,
                    // along with its page ranges
                    .build();

            // Creates a new job instance
            CombinePDFJob combinePDFJob = new CombinePDFJob(combinePDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(combinePDFJob);
            PDFServicesResponse<CombinePDFResult> pdfServicesResponse = pdfServices.getJobResult(location, CombinePDFResult.class);

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

    private static PageRanges getPageRangeForSecondFile() {
        // Specify which pages of the second file are to be included in the combined file
        PageRanges pageRangesForSecondFile = new PageRanges();
        // Add all pages including and after page 3
        pageRangesForSecondFile.addAllFrom(3);
        return pageRangesForSecondFile;
    }

    private static PageRanges getPageRangeForFirstFile() {
        // Specify which pages of the first file are to be included in the combined file
        PageRanges pageRangesForFirstFile = new PageRanges();
        // Add page 1
        pageRangesForFirstFile.addSinglePage(1);
        // Add page 2
        pageRangesForFirstFile.addSinglePage(2);
        // Add pages 3 to 4
        pageRangesForFirstFile.addRange(3, 4);
        return pageRangesForFirstFile;
    }

    // Generates a string containing a directory structure and file name for the output file
    public static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/CombinePDFWithPageRanges"));
        return ("output/CombinePDFWithPageRanges/combine" + timeStamp + ".pdf");
    }
}
