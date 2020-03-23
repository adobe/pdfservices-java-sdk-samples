# Samples for the DC Services Java SDK

This sample project helps you get started with the DC services SDK.

The sample classes illustrate how to perform PDF-related actions (such as converting to and from the PDF format) using 
the SDK. **Please note that the DC Services SDK supports only server side use cases.**

## Prerequisites
The sample application has the following requirements:
* Java JDK : Version 8 or above.
* Build Tool: The application requires Maven to be installed. Maven installation instructions can be found 
[here](https://maven.apache.org/install.html).


## Authentication Setup

The credentials file and corresponding private key file for the samples is ```dc-services-sdk-credentials.json``` and ```private.key``` 
respectively. Before the samples can be run, replace both the files with the ones present in the downloaded zip file at 
the end of creation of credentials via [Get Started](https://www.adobe.io/apis/documentcloud/dcsdk/gettingstarted.html?ref=getStartedWithServicesSdk) workflow.

The SDK also supports providing the authentication credentials at runtime, without storing them in a config file. Please
refer this [section](#create-a-pdf-file-from-a-docx-file-by-providing-in-memory-authentication-credentials) to 
know more.

## Quota Exhaustion

If you receive ServiceUsageException during the Samples run, it means that trial credentials have exhausted their quota 
of 5000 pages. Please contact [here](https://www.adobe.com/go/dcsdk_requestform) to get the paid credentials.

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

####  Create a PDF File From a DOCX Input Stream

The sample class CreatePDFFromDOCXInputStream creates a PDF file from a DOCX input stream.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromDOCXInputStream 
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

#### Create a PDF File From Static HTML (via Zip Archive)

The sample class CreatePDFFromStaticHTML creates a PDF file from a zip file containing the input HTML file and its resources. 
Please refer the documentation of CreatePDFOperation.java to see instructions on the structure of the zip file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromStaticHTML 
```

#### Create a PDF File From Dynamic HTML (via Zip Archive)

The sample class CreatePDFFromDynamicHTML converts a zip file, containing the input HTML file and its resources, along 
with the input data to a PDF file. The input data is used by the javascript in the HTML file to manipulate the HTML DOM, 
thus effectively updating the source HTML file. This mechanism can be used to provide data to the template HTML 
dynamically and then, convert it into a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFFromDynamicHTML 
```

#### Create a PDF File From a DOCX File (By providing in-memory Authentication credentials)

The sample class ```CreatePDFWithInMemoryAuthCredentials``` highlights how to provide in-memory auth credentials
for performing an operation. This enables the clients to fetch the credentials from a secret server during runtime, 
instead of storing them in a file.

Before running the sample, authentication credentials need to be updated as per the instructions in the class. 
```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFWithInMemoryAuthCredentials 
```

#### Create a PDF File From a DOCX File (By providing custom value for timeouts)

The sample project CreatePDFWithCustomTimeouts highlights how to provide the custom value for connection timeout and socket timeout.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.createpdf.CreatePDFWithCustomTimeouts
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

### OCR PDF file

These samples illustrates how to apply OCR(Optical Character Recognition) to a PDF file and convert it to a searchable copy of your PDF. The supported input format is application/pdf.

#### Convert PDF File to a searchable PDF file.

The sample project OcrPDF converts a PDF file into a searchable PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.ocr.OcrPDF
```

#### Convert PDF file into a searchable file while keeping the original image.

The sample project OcrPDFWithOptions converts a PDF file to a searchable PDF file with maximum fidelity to the original 
image and default en-us locale. Refer to the documentation of OCRSupportedLocale and OCRSupportedType to see 
the list of supported OCR locales and OCR types.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.platform.operation.samples.ocr.OcrPDFWithOptions
```

### Licensing

This project is licensed under the MIT License. See [LICENSE](LICENSE.md) for more information.
