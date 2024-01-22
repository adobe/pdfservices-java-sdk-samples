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
import java.util.Arrays;

/**
 * This sample illustrates how to generate a tagged PDF by setting options with command line arguments.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class AutotagPDFParameterised {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutotagPDFParameterised.class);

    public static void main(String[] args) {

        try (InputStream inputStream = Files.newInputStream(new File(getInputFilePathFromCmdArgs(args)).toPath())) {
            LOGGER.info("--input " + getInputFilePathFromCmdArgs(args));
            LOGGER.info("--output " + getOutputFilePathFromCmdArgs(args));
            LOGGER.info("--report " + getGenerateReportFromCmdArgs(args));
            LOGGER.info("--shift_headings " + getShiftHeadingsFromCmdArgs(args));

            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());

            // Create parameters for the job
            AutotagPDFParams autotagPDFParams = getOptionsFromCmdArgs(args);

            // Creates a new job instance
            AutotagPDFJob autotagPDFJob = new AutotagPDFJob(asset).setParams(autotagPDFParams);

            // Submit the job and gets the job result
            String location = pdfServices.submit(autotagPDFJob);
            PDFServicesResponse<AutotagPDFResult> pdfServicesResponse = pdfServices.getJobResult(location, AutotagPDFResult.class);

            // Get content from the resulting asset(s)
            Asset resultAsset = pdfServicesResponse.getResult().getTaggedPDF();
            Asset resultAssetReport = pdfServicesResponse.getResult().getReport();
            StreamAsset streamAsset = pdfServices.getContent(resultAsset);
            StreamAsset streamAssetReport = (autotagPDFParams != null && autotagPDFParams.isGenerateReport()) ? pdfServices.getContent(resultAssetReport) : null;

            // Creating output streams and copying stream assets' content to it
            String outputPath = getOutputFilePathFromCmdArgs(args);
            String outputFilePath = outputPath + "autotagPDFInput-tagged.pdf";
            LOGGER.info(String.format("Saving asset at %s", outputFilePath));

            OutputStream outputStream = Files.newOutputStream(new File(outputFilePath).toPath());
            IOUtils.copy(streamAsset.getInputStream(), outputStream);
            outputStream.close();
            if (streamAssetReport != null) {
                String outputFilePathReport = outputPath + "autotagPDFInput-report.xlsx";
                LOGGER.info(String.format("Saving asset at %s", outputFilePathReport));

                OutputStream outputStreamReport = Files.newOutputStream(new File(outputFilePathReport).toPath());
                IOUtils.copy(streamAssetReport.getInputStream(), outputStreamReport);
                outputStreamReport.close();
            }
        } catch (ServiceApiException | IOException | SDKException | ServiceUsageException e) {
            LOGGER.error("Exception encountered while executing operation", e);
        }
    }

    private static AutotagPDFParams getOptionsFromCmdArgs(String[] args) {
        Boolean generateReport = getGenerateReportFromCmdArgs(args);
        Boolean shiftHeadings = getShiftHeadingsFromCmdArgs(args);
        AutotagPDFParams.Builder autotagPDFParamsBuilder = AutotagPDFParams.autotagPDFParamsBuilder();

        if (generateReport)
            autotagPDFParamsBuilder.generateReport();
        if (shiftHeadings)
            autotagPDFParamsBuilder.shiftHeadings();

        return autotagPDFParamsBuilder.build();
    }

    private static Boolean getShiftHeadingsFromCmdArgs(String[] args) {
        return Arrays.asList(args).contains("--shift_headings");
    }

    private static Boolean getGenerateReportFromCmdArgs(String[] args) {
        return Arrays.asList(args).contains("--report");
    }

    private static String getInputFilePathFromCmdArgs(String[] args) {
        String inputFilePath = "src/main/resources/autotagPDFInput.pdf";
        int inputFilePathIndex = Arrays.asList(args).indexOf("--input");
        if (inputFilePathIndex >= 0 && inputFilePathIndex < args.length - 1) {
            inputFilePath = args[inputFilePathIndex + 1];
        } else
            LOGGER.info("input file not specified, using default value : autotagPDFInput.pdf");

        return inputFilePath;
    }

    private static String getOutputFilePathFromCmdArgs(String[] args) throws IOException {
        String outputFilePath = "output/AutotagPDFParameterised/";
        int outputFilePathIndex = Arrays.asList(args).indexOf("--output");
        if (outputFilePathIndex >= 0 && outputFilePathIndex < args.length - 1) {
            outputFilePath = args[outputFilePathIndex + 1];
        } else
            LOGGER.info("output path not specified, using default value : " + outputFilePath);

        Files.createDirectories(Paths.get(outputFilePath));
        return outputFilePath;
    }
}
