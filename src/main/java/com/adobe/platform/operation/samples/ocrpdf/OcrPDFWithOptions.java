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

package com.adobe.platform.operation.samples.ocrpdf;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.OCROperation;
import com.adobe.platform.operation.pdfops.options.ocr.OCROptions;
import com.adobe.platform.operation.pdfops.options.ocr.OCRSupportedLocale;
import com.adobe.platform.operation.pdfops.options.ocr.OCRSupportedType;

/**
 * This sample illustrates how to perform an OCR operation on a PDF file and convert it into an searchable PDF file on
 * the basis of provided locale and SEARCHABLE_IMAGE_EXACT ocr type to keep the original image
 * (Recommended for cases requiring maximum fidelity to the original image.).
 * <p>
 * Note that OCR operation on a PDF file results in a PDF file.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class OcrPDFWithOptions {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(OcrPDFWithOptions.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdftools-api-credentials.json")
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials);
            OCROperation ocrOperation = OCROperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/ocrInput.pdf");
            ocrOperation.setInput(source);

            // Build OCR options from supported locales and OCR-types and set them into the operation
            OCROptions ocrOptions = OCROptions.ocrOptionsBuilder()
                    .withOCRLocale(OCRSupportedLocale.EN_US)
                    .withOCRType(OCRSupportedType.SEARCHABLE_IMAGE_EXACT)
                    .build();
            ocrOperation.setOptions(ocrOptions);

            // Execute the operation
            FileRef result = ocrOperation.execute(executionContext);

            // Save the result at the specified location
            result.saveAs("output/ocrWithOptionsOutput.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
