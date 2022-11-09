package com.adobe.pdfservices.operation.samples.misc;

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

public class CreatePDFWithAuthenticatedProxyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFWithAuthenticatedProxyServer.class);

    public static void main(String[] args) {

        try {

            // Initial setup, create credentials instance.
            Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
                    .fromFile("pdfservices-api-credentials.json")
                    .build();

            /*
            Initial setup, Create client config instance with proxy server configuration.
            Replace the values of PROXY_HOSTNAME with the proxy server hostname.
            Replace the username and password with Proxy Authentication credentials.
            If the scheme of proxy server is not HTTPS then, replace ProxyScheme parameter with HTTP.
            If the port for proxy server is diff than the default port for HTTP and HTTPS, then please set the PROXY_PORT,
                else, remove its setter statement.
            */
            ProxyServerConfig proxyServerConfig = new ProxyServerConfig.Builder()
                    .withHost("10.122.98.98")
                    .withProxyScheme(ProxyScheme.HTTP) // Replace it with HTTPS if the proxy server scheme is https
                    .withPort(3128)
                    .withCredentials(new UsernamePasswordCredentials("user1", "password"))
                    .build();

            ClientConfig clientConfig = ClientConfig.builder()
                    .withConnectTimeout(10000)
                    .withProxyServerConfig(proxyServerConfig)
                    .withSocketTimeout(40000)
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
