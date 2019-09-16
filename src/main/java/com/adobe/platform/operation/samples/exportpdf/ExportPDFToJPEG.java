/*
 * Copyright 2019 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.platform.operation.samples.exportpdf;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ClientContext;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.ExportPDFOperation;
import com.adobe.platform.operation.pdfops.options.exportpdf.ExportPDFTargetFormat;

/**
 * This sample illustrates how to export a PDF file to JPEG.
 * <p>
 * Note that exporting a PDF file to an image format results in a ZIP archive containing one image per page.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ExportPDFToJPEG {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPDFToJPEG.class);

    public static void main(String[] args) {
        try {

            // Initial setup, create a ClientContext using a config file, and a new operation instance by specifying the intended export format.
            ClientContext clientContext = ClientContext.createFromFile("dc-services-sdk-config.json");
            ExportPDFOperation exportPdfOperation = ExportPDFOperation.createNew(ExportPDFTargetFormat.JPEG);

            // Set operation input from a source file.
            FileRef sourceFileRef = FileRef.createFromLocalFile("src/main/resources/exportPDFToImageInput.pdf");
            exportPdfOperation.setInput(sourceFileRef);

            // Execute the operation.
            FileRef result = exportPdfOperation.execute(clientContext);

            // Save the result to the specified location.
            result.saveAs("output/exportPDFToJPEG.zip");

        } catch (ServiceApiException | IOException | SdkException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
