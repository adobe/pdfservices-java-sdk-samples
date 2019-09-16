# Samples for the DC Services Java SDK

This sample project helps you get started with the DC services SDK.

The sample classes illustrate how to perform PDF-related actions (such as converting to and from the PDF format) using 
the SDK.

## Prerequisites
The sample application has the following requirements:
* Java JDK : Version 8 or above.
* Build Tool: The application requires Maven to be installed. Maven installation instructions can be found 
[here](https://maven.apache.org/install.html).


## Authentication Setup

The configuration file for the samples is ```dc-services-sdk-config.json```. Before the samples can be run, replace this 
file with the dc-services-sdk-config.json you receive from Adobe when you submit the early access request form.

The SDK also supports providing the authentication credentials at runtime, without storing them in a config file. Please
refer this [section](#create-a-pdf-file-from-a-docx-file-by-providing-in-memory-authentication-credentials) to 
know more.

## Build with maven

Run the following command to build the project:
```$xslt
mvn clean install
```

Note that the DC Services SDK is listed as a dependency in the pom.xml and will be downloaded automatically.

## A Note on Logging
For logging, this SDK uses the [slf4j API](https://www.slf4j.org/) with a log4j2-slf4j binding. The logging configurations 
are provided in ```src/main/resources/log4j2.properties```. Alternate bindings, if required, can be specified in pom.xml.

## Running the samples

The following sub-sections describe how to run the samples. Prior to running the samples, check that the configuration 
file is set up as described above and that the project has been built.

The code itself is in the ```com.adobe.platform.operation.samples``` package under the ```src/main/java/``` folder. Test 
files used by the samples can be found in ```src/main/resources/```. When executed, all samples create an ```output``` 
child folder under the working directory to store their results.

### Create a PDF File
These samples illustrate how to convert files of some formats to PDF. Refer the documentation of CreatePDFOperation.java 
to see the list of all supported media types which can be converted to PDF.

####  Create a PDF File From a DOCX File 

The sample class CreatePDFFromDOCX creates a PDF file from a DOCX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromDOCX 
```

####  Create a PDF File From a DOCX File (Write to an OutputStream)

The sample class CreatePDFFromDOCXToOutputStream creates a PDF file from a DOCX file. Instead of saving the result to a 
local file, it writes the result to an output stream.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromDOCXToOutputStream 
```


####  Create a PDF File From a PPTX File 

The sample class CreatePDFFromPPTX creates a PDF file from a PPTX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromPPTX
```

#### Create a PDF File From HTML (via Zip Archive)

The sample class CreatePDFFromHTML creates a PDF file from a zip file containing the input HTML file and its resources. 
Please refer the documentation of CreatePDFOperation.java to see instructions on the structure of the zip file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromHTML 
```

#### Create a PDF File From HTML (via URL)

The sample class CreatePDFFromURL converts an HTML page specified by a URL to a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromURL 
```

#### Create a PDF File From a DOCX File (By providing in-memory Authentication credentials)

The sample class ```CreatePDFWithInMemoryAuthCredentials``` highlights how to provide in-memory auth credentials
for performing an operation. This enables the clients to fetch the credentials from a secret server during runtime, 
instead of storing them in a file.

Before running the sample, authentication credentials need to be updated as per the instructions in the class. 
```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFWithInMemoryAuthCredentials 
```

### Export PDF To Other Formats
These samples illustrate how to export PDF files to other formats. Refer to the documentation of ExportPDFOperation.java
to see the list of supported export formats.

#### Export a PDF File To a DOCX File 

The sample class ExportPDFToDOCX converts a PDF file to a DOCX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.exportpdf.ExportPDFToDOCX
```

#### Export a PDF File To an Image Format (JPEG)

The sample class ExportPDFToJPEG converts a PDF file's pages to JPEG images. Note that the output is a zip archive 
containing the individual images.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.exportpdf.ExportPDFToJPEG
```

### Combine PDF Files
These samples illustrate how to combine multiple PDF files into a single PDF file.

#### Combine Multiple PDF Files

The sample class CombinePDF combines multiple PDF files into a single PDF file. The combined PDF file contains all pages
of the source files.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.combine.CombinePDF 
```

#### Combine Specific Pages of Multiple PDF Files

The sample class CombinePDFWithPageRanges combines specific pages of multiple PDF files into into a single PDF file.
 
```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.combine.CombinePDFWithPageRanges 
```

### Licensing

This project is licensed under the MIT License. See [LICENSE](LICENSE) for more information.
