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

package com.adobe.pdfservices.operation.samples.createpdf;

import com.adobe.pdfservices.operation.ClientConfig;
import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CreatePDFOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This sample illustrates how to setup Proxy Server configurations for performing an operation. This enables the
 * clients to set proxy server configurations to enable the API calls in a network where calls are blocked unless they
 * are routed via Proxy server.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */

public class CreatePDFWithProxyServer {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFWithProxyServer.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            /*
            Initial setup, Create client config instance with proxy server configuration.
            Replace the values of PROXY_HOSTNAME with the proxy server hostname.
            If the scheme of proxy server is not HTTPS then, replace ProxyScheme parameter with HTTP.
            If the port for proxy server is diff than the default port for HTTP and HTTPS, then please set the PROXY_PORT,
                else, remove its setter statement.
            */
            ClientConfig clientConfig = ClientConfig.builder()
                    .withConnectTimeout(10000)
                    .withSocketTimeout(40000)
                    .withProxyScheme(ClientConfig.ProxyScheme.HTTPS) // Replace it with HTTP if the proxy server scheme is http
                    .withProxyHost("PROXY_HOSTNAME")
                    .withProxyPort(443)
                    .build();

            //Create an ExecutionContext using credentials and create a new operation instance.
            ExecutionContext executionContext = ExecutionContext.create(credentials, clientConfig);
            CreatePDFOperation createPDFOperation = CreatePDFOperation.createNew();

            // Set operation input from a source file.
            FileRef source = FileRef.createFromLocalFile("src/main/resources/createPDFInput.docx");
            createPDFOperation.setInput(source);

            // Execute the operation.
            FileRef result = createPDFOperation.execute(executionContext);

            // Save the result to the specified location.
            result.saveAs("output/createPDFWithProxyServer.pdf");

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }
}
