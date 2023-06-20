/*
 * Copyright 2023 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */

package com.adobe.pdfservices.operation.samples.customconfigurations;

import com.adobe.pdfservices.operation.ClientConfig;
import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CreatePDFOperation;
import com.adobe.pdfservices.operation.proxy.ProxyScheme;
import com.adobe.pdfservices.operation.proxy.ProxyServerConfig;
import com.adobe.pdfservices.operation.proxy.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to set up authenticated Proxy Server configurations for performing an operation. This enables the
 * clients to set proxy server configurations to enable the API calls in a network where calls are blocked unless they
 * are routed via an authenticated Proxy server.
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class CreatePDFWithAuthenticatedProxyServer {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFWithAuthenticatedProxyServer.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.servicePrincipalCredentialsBuilder()
                    .withClientId(System.getenv("PDF_SERVICES_CLIENT_ID"))
                    .withClientSecret(System.getenv("PDF_SERVICES_CLIENT_SECRET"))
                    .build();

            /*
            Initial setup, Create proxy server config instance for client config.
            Replace the values of PROXY_HOSTNAME with the proxy server hostname.
            Replace the username and password with Proxy Server Authentication credentials.
            If the scheme of proxy server is not HTTP then, replace ProxyScheme parameter with HTTPS.
            If the port for proxy server is diff than the default port for HTTP and HTTPS, then please set the PROXY_PORT,
            else, remove its setter statement.
            */
            ProxyServerConfig proxyServerConfig = new ProxyServerConfig.Builder()
                    .withHost("PROXY_HOSTNAME")
                    .withProxyScheme(ProxyScheme.HTTP) // Replace it with HTTPS if the proxy server scheme is https
                    .withPort(443)
                    .withCredentials(new UsernamePasswordCredentials("USERNAME", "PASSWORD"))
                    .build();

            // Create client config instance
            ClientConfig clientConfig = ClientConfig.builder()
                    .withConnectTimeout(10000)
                    .withSocketTimeout(40000)
                    .withProxyServerConfig(proxyServerConfig)
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
            String outputFilePath = createOutputFilePath();
            result.saveAs(outputFilePath);

        } catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
            LOGGER.error("Exception encountered while executing operation", ex);
        }
    }

    //Generates a string containing a directory structure and file name for the output file.
    public static String createOutputFilePath(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(now);
        return("output/CreatePDFWithAuthenticatedProxyServer/create" + timeStamp + ".pdf");
    }
}
