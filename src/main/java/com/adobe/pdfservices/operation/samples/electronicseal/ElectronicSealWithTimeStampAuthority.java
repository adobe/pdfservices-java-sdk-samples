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

package com.adobe.pdfservices.operation.samples.electronicseal;

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
import com.adobe.pdfservices.operation.pdfjobs.jobs.PDFElectronicSealJob;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.CSCAuthContext;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.CertificateCredentials;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.DocumentLevelPermission;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.FieldLocation;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.FieldOptions;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.PDFElectronicSealParams;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.RFC3161TSAOptions;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.TSABasicAuthCredentials;
import com.adobe.pdfservices.operation.pdfjobs.params.electronicseal.TSAOptions;
import com.adobe.pdfservices.operation.pdfjobs.result.PDFElectronicSealResult;
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
 * This sample illustrates how to apply electronic seal over the PDF document using time stamp authority options.
 *
 * <p>
 * To know more about PDF Electronic Seal, please see the
 * <a href="https://www.adobe.com/go/dc_eseal_overview_doc" target="_blank">documentation</a>.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ElectronicSealWithTimeStampAuthority {

    // Initialize the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ElectronicSealWithTimeStampAuthority.class);

    public static void main(String[] args) {
        try (
                InputStream inputStream = Files.newInputStream(new File("src/main/resources/sampleInvoice.pdf").toPath());
                InputStream inputStreamSealImage = Files.newInputStream(new File("src/main/resources/sampleSealImage.png").toPath())) {
            // Initial setup, create credentials instance
            Credentials credentials = new ServicePrincipalCredentials(System.getenv("PDF_SERVICES_CLIENT_ID"), System.getenv("PDF_SERVICES_CLIENT_SECRET"));

            // Creates a PDF Services instance
            PDFServices pdfServices = new PDFServices(credentials);

            // Creates an asset(s) from source file(s) and upload
            Asset asset = pdfServices.upload(inputStream, PDFServicesMediaType.PDF.getMediaType());
            Asset sealImageAsset = pdfServices.upload(inputStreamSealImage, PDFServicesMediaType.PNG.getMediaType());

            // Set the document level permission to be applied for output document
            DocumentLevelPermission documentLevelPermission = DocumentLevelPermission.FORM_FILLING;

            // Sets the Seal Field Name to be created in input PDF document.
            String sealFieldName = "Signature1";

            // Sets the page number in input document for applying seal.
            Integer sealPageNumber = 1;

            // Sets if seal should be visible or invisible.
            Boolean sealVisible = true;

            //Creates FieldLocation instance and set the coordinates for applying signature
            FieldLocation fieldLocation = new FieldLocation(150, 250, 350, 200);

            //Create FieldOptions instance with required details.
            FieldOptions fieldOptions = new FieldOptions.Builder(sealFieldName)
                    .setFieldLocation(fieldLocation)
                    .setPageNumber(sealPageNumber)
                    .setVisible(sealVisible)
                    .build();

            // Sets the name of TSP Provider being used.
            String providerName = "<PROVIDER_NAME>";

            // Sets the access token to be used to access TSP provider hosted APIs.
            String accessToken = "<ACCESS_TOKEN>";

            // Sets the credential ID.
            String credentialID = "<CREDENTIAL_ID>";

            // Sets the PIN generated while creating credentials.
            String pin = "<PIN>";

            //Creates CSCAuthContext instance using access token and token type.
            CSCAuthContext cscAuthContext = new CSCAuthContext(accessToken, "Bearer");

            //Create CertificateCredentials instance with required certificate details.
            CertificateCredentials certificateCredentials = CertificateCredentials.cscCredentialBuilder()
                    .withProviderName(providerName)
                    .withCredentialID(credentialID)
                    .withPin(pin)
                    .withCSCAuthContext(cscAuthContext)
                    .build();

            //Create TSABasicAuthCredentials using username and password
            TSABasicAuthCredentials tsaBasicAuthCredentials = new TSABasicAuthCredentials("<USERNAME>", "<PASSWORD>");

            // Set the Time Stamp Authority Options using url and TSA Auth credentials
            TSAOptions tsaOptions = new RFC3161TSAOptions("<TIMESTAMP_URL>", tsaBasicAuthCredentials);

            // Create parameters for the job
            PDFElectronicSealParams pdfElectronicSealParams = PDFElectronicSealParams.pdfElectronicSealParamsBuilder(certificateCredentials, fieldOptions)
                    .withDocumentLevelPermission(documentLevelPermission)
                    .withTSAOptions(tsaOptions)
                    .build();

            // Creates a new job instance
            PDFElectronicSealJob pdfElectronicSealJob = new PDFElectronicSealJob(asset, pdfElectronicSealParams);

            // Sets the optional input seal image for PDFElectronicSealOperation instance
            pdfElectronicSealJob.setSealImageAsset(sealImageAsset);

            // Submit the job and gets the job result
            String location = pdfServices.submit(pdfElectronicSealJob);
            PDFServicesResponse<PDFElectronicSealResult> pdfServicesResponse = pdfServices.getJobResult(location, PDFElectronicSealResult.class);

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

    // Generates a string containing a directory structure and file name for the output file
    private static String createOutputFilePath() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        Files.createDirectories(Paths.get("output/ElectronicSeal"));
        return ("output/ElectronicSeal/sealedOutputWithTimeStampAuthority" + timeStamp + ".pdf");
    }
}
