package com.adobe.pdfservices.operation.samples.eseal;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.DigitalSealOperation;
import com.adobe.pdfservices.operation.pdfops.options.digitalseal.*;
import com.adobe.pdfservices.operation.samples.documentmerge.MergeDocumentToPDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample class use default appearance options and takes required sealing parameters as the input to generate the
 * PDF document with seal as the output.
 * <p>
 * To know more about PDF Electronic Seal, refer to the <a href="http://www.adobe.com/go/dceseal_overview_doc">documentation</a>
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class ESealWithDefaultAppearanceOptions {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(MergeDocumentToPDF.class);

    public static void main(String[] args) {
        try {
            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            // Create an ExecutionContext using credentials.
            ExecutionContext executionContext = ExecutionContext.create(credentials);

            //Get the input document to perform the sealing operation
            FileRef sourceFile = FileRef.createFromLocalFile("src/main/resources/Invoice.pdf");

            //Get the background seal image for signature , if required.
            FileRef sealImageFile = FileRef.createFromLocalFile("src/main/resources/sealImage.jpeg");

            //Set the Seal Field Name to be created in input PDF document.
            String signFieldName = "SealField";
            //Set the page number in input document for applying seal.
            int signPageNumber = 1;
            //Set if seal should be visible or invisible.
            boolean sealVisible = true;
            //Create SealFieldLocationOptions instance and set the coordinates for applying signature
            SealFieldLocationOptions signatureLocation = new SealFieldLocationOptions(150, 250, 350, 200);
            //Create SealFieldOptions instance with required details.
            SealFieldOptions sealFieldOptions = new SealFieldOptions.Builder(signatureLocation, signPageNumber, signFieldName)
                    .setVisible(sealVisible)
                    .build();

            //Set the name of TSP Provider being used.
            String providerName = "intesi";
            //Set the access token to be used to access TSP provider hosted APIs.
            String accessToken = "ae23b89f-9550-4352-804a-f40e5ac36c64";
            //Set the credential ID.
            String credentialID = "[ADOBE_TEST]_AUTO_12834_SIGN_1647837397700:44";
            //Set the PIN generated while creating credentials.
            String credentialPin = "12345678";
            //Create SealCredentialOptions instance with required certificate details.
            SealCredentialOptions sealCredentialOptions = new  CSCCredentialOptions.Builder(providerName, credentialID, credentialPin, accessToken).setTokenType("Bearer").build();
            //Create SealingOptions instance with all the sealing parameters.
            SealOptions sealOptions = new SealOptions.Builder(SignatureType.SIGN, SignatureFormat.PKCS7, sealCredentialOptions,
                    sealFieldOptions).build();

            //Create a DigitalSealOptions instance using the SealOptions instance
            DigitalSealOptions digitalSealOptions = new DigitalSealOptions(sealOptions);

            //Create the DigitalSealOperation instance using the digitalSealOptions instance
            DigitalSealOperation digitalSealOperation = DigitalSealOperation.createNew(digitalSealOptions);

            //Set the input source file for digitalSealOperation instance
            digitalSealOperation.setInputDocument(sourceFile);

            //Set the optional input seal image for digitalSealOperation instance
            digitalSealOperation.setSealImage(sealImageFile);

            //Execute the operation
            FileRef result = digitalSealOperation.execute(executionContext);

            //Save the output at specified location
            result.saveAs("output/sealedOutputWithDefaultAppearanceOptions.pdf");


        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
