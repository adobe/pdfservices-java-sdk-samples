/*
 * Copyright 2022 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it.If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.autotagpdf;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.AutotagPDFOperation;
import com.adobe.pdfservices.operation.pdfops.options.autotag.AutotagOutputFiles;
import com.adobe.pdfservices.operation.pdfops.options.autotag.AutotagPDFOptions;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * This sample illustrates how to generate a tagged PDF by setting options with command line arguments.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class AutotagPDFParamaterised {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AutotagPDFWithOptions.class);

    public static void main(String[] args) {
        LOGGER.info("--input " + getInputFilePathFromCmdArgs(args));
        LOGGER.info("--output " + getOutputFilePathFromCmdArgs(args));
        LOGGER.info("--report " + getGenerateReportFromCmdArgs(args));
        LOGGER.info("--shift_headings " + getShiftHeadingsFromCmdArgs(args));

        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            AutotagPDFOperation autotagPDFOperation = AutotagPDFOperation.createNew();

            // Set input for operation from command line args
            autotagPDFOperation.setInput(FileRef.createFromLocalFile(getInputFilePathFromCmdArgs(args)));

            // Get and Build AutotagPDF options from command line args and set them into the operation
            AutotagPDFOptions autotagPDFOptions = getOptionsFromCmdArgs(args);
            autotagPDFOperation.setOptions(autotagPDFOptions);

            // Execute the operation
            AutotagOutputFiles autotagOutputFiles = autotagPDFOperation.execute(executionContext);

            // Save the output files at the specified location
            String outputPath = getOutputFilePathFromCmdArgs(args);
            autotagOutputFiles.saveTaggedPDF(outputPath + "autotagPdfInput-tagged.pdf");
            if (autotagPDFOptions != null && autotagPDFOptions.isGenerateReport())
                autotagOutputFiles.saveReport(outputPath + "autotagPdfInput-report.xlsx");

        } catch (ServiceApiException | IOException | ServiceUsageException e) {
            System.out.println(e);
        }
    }

    private static AutotagPDFOptions getOptionsFromCmdArgs(String[] args) {
        Boolean generateReport = getGenerateReportFromCmdArgs(args);
        Boolean shiftHeadings = getShiftHeadingsFromCmdArgs(args);

        AutotagPDFOptions.Builder builder = AutotagPDFOptions.autotagPDFOptionsBuilder();

        if (generateReport)
            builder.generateReport();
        if (shiftHeadings)
            builder.shiftHeadings();

        return builder.build();
    }

    private static Boolean getShiftHeadingsFromCmdArgs(String[] args) {
        return Arrays.asList(args).contains("--shift_headings");
    }

    private static Boolean getGenerateReportFromCmdArgs(String[] args) {
        return Arrays.asList(args).contains("--report");
    }

    private static String getInputFilePathFromCmdArgs(String[] args) {
        String inputFilePath = "src/main/resources/autotagPdfInput.pdf";
        int inputFilePathIndex = Arrays.asList(args).indexOf("--input");
        if (inputFilePathIndex >= 0 && inputFilePathIndex < args.length - 1) {
            inputFilePath = args[inputFilePathIndex + 1];
        } else
            LOGGER.info("input file not specified, using default value : autotagPdfInput.pdf");

        return inputFilePath;
    }

    private static String getOutputFilePathFromCmdArgs(String[] args) {
        String outputFilePath = "output/AutotagPDFParamaterised/";
        int outputFilePathIndex = Arrays.asList(args).indexOf("--output");
        if (outputFilePathIndex >= 0 && outputFilePathIndex < args.length - 1) {
            outputFilePath = args[outputFilePathIndex + 1];
        } else
            LOGGER.info("output path not specified, using default value : " + outputFilePath);

        return outputFilePath;
    }
}
