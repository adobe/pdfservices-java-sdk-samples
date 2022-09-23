# Samples for the PDF Services Java SDK

This sample project helps you get started with the PDF Services Java SDK.

The sample classes illustrate how to perform PDF-related actions (such as converting to and from the PDF format) using
the SDK. **Please note that the PDF Services Java SDK supports only server side use cases.**

## Prerequisites
The sample application has the following requirements:
* Java JDK : Version 8 or above.
* Build Tool: The application requires Maven to be installed. Maven installation instructions can be found
  [here](https://maven.apache.org/install.html).


## Authentication Setup

The credentials file and corresponding private key file for the samples is ```pdfservices-api-credentials.json``` and ```private.key```
respectively. Before the samples can be run, replace both the files with the ones present in the downloaded zip file at
the end of creation of credentials via [Get Started](https://www.adobe.io/apis/documentcloud/dcsdk/gettingstarted.html?ref=getStartedWithServicesSdk) workflow.

The SDK also supports providing the authentication credentials at runtime, without storing them in a config file. Please
refer this [section](#create-a-pdf-file-from-a-docx-file-by-providing-in-memory-authentication-credentials) to
know more.

## Client Configurations

The SDK supports setting up custom socket timeout or connect timeout for the API calls. Please
refer this [section](#create-a-pdf-file-from-a-docx-file-by-providing-custom-value-for-timeouts) to
know more.

The SDK also supports setting up Proxy Server configurations which helps in successful API calls for network where all outgoing calls have to go through a proxy else, they are blocked. Please
refer this [section](#create-a-pdf-file-from-a-docx-file-by-providing-proxy-server-settings) to
know more.

## Quota Exhaustion

If you receive ServiceUsageException during the Samples run, it means that trial credentials have exhausted their usage
quota. Please [contact us](https://www.adobe.com/go/pdftoolsapi_requestform) to get paid credentials.

## Build with maven

Run the following command to build the project:
```$xslt
mvn clean install
```

Note that the PDF Services SDK is listed as a dependency in the pom.xml and will be downloaded automatically.

## A Note on Logging
For logging, this SDK uses the [slf4j API](https://www.slf4j.org/) with a log4j2-slf4j binding. The logging configurations
are provided in ```src/main/resources/log4j2.properties```. Alternate bindings, if required, can be specified in pom.xml.

## Running the samples

The following sub-sections describe how to run the samples. Prior to running the samples, check that the configuration
file is set up as described above and that the project has been built.

The code itself is in the ```com.adobe.pdfservices.operation.samples``` package under the ```src/main/java/``` folder. Test
files used by the samples can be found in ```src/main/resources/```. When executed, all samples create an ```output```
child folder under the working directory to store their results.

### Create a PDF File
These samples illustrate how to convert files of some formats to PDF. Refer the documentation of CreatePDFOperation.java
to see the list of all supported media types which can be converted to PDF.

####  Create a PDF File From a DOCX File

The sample class CreatePDFFromDOCX creates a PDF file from a DOCX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromDOCX 
```

####  Create a PDF File From a DOCX File with options

The sample class CreatePDFFromDOCXWithOptions creates a PDF file from a DOCX file by setting documentLanguage as
the language of input file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromDOCXWithOptions 
```

####  Create a PDF File From a DOCX Input Stream

The sample class CreatePDFFromDOCXInputStream creates a PDF file from a DOCX input stream.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromDOCXInputStream 
```

####  Create a PDF File From a DOCX File (Write to an OutputStream)

The sample class CreatePDFFromDOCXToOutputStream creates a PDF file from a DOCX file. Instead of saving the result to a
local file, it writes the result to an output stream.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromDOCXToOutputStream 
```


####  Create a PDF File From a PPTX File

The sample class CreatePDFFromPPTX creates a PDF file from a PPTX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromPPTX
```

#### Create a PDF File From a Static HTML file with inline CSS

The sample class CreatePDFFromHTMLWithInlineCSS creates a PDF file from an input HTML file with inline CSS.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromHTMLWithInlineCSS 
```

#### Create a PDF File From HTML specified via URL

The sample class CreatePDFFromURL creates a PDF file from an HTML specified via URL.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromURL 
```

#### Create a PDF File From Static HTML (via Zip Archive)

The sample class CreatePDFFromStaticHTML creates a PDF file from a zip file containing the input HTML file and its resources.
Please refer the documentation of CreatePDFOperation.java to see instructions on the structure of the zip file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromStaticHTML 
```

#### Create a PDF File From Dynamic HTML (via Zip Archive)

The sample class CreatePDFFromDynamicHTML converts a zip file, containing the input HTML file and its resources, along
with the input data to a PDF file. The input data is used by the javascript in the HTML file to manipulate the HTML DOM,
thus effectively updating the source HTML file. This mechanism can be used to provide data to the template HTML
dynamically and then, convert it into a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFFromDynamicHTML 
```

#### Create a PDF File From a DOCX File (By providing in-memory Authentication credentials)

The sample class ```CreatePDFWithInMemoryAuthCredentials``` highlights how to provide in-memory auth credentials
for performing an operation. This enables the clients to fetch the credentials from a secret server during runtime,
instead of storing them in a file.

Before running the sample, authentication credentials need to be updated as per the instructions in the class.
```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFWithInMemoryAuthCredentials 
```

#### Create a PDF File From a DOCX File (By providing custom value for timeouts)

The sample class CreatePDFWithCustomTimeouts highlights how to provide the custom value for connection timeout and socket timeout.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFWithCustomTimeouts
```

#### Create a PDF File From a DOCX File (By providing Proxy Server settings)

The sample class CreatePDFWithPorxyServer highlights how to provide Proxy Server configurations to allow all API calls via that proxy Server.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.createpdf.CreatePDFWithProxyServer
```

### Export PDF To Other Formats
These samples illustrate how to export PDF files to other formats. Refer to the documentation of ExportPDFOperation.java
and ExportPDFToImagesOperation.java for supported export formats.

#### Export a PDF File To a DOCX File

The sample class ExportPDFToDOCX converts a PDF file to a DOCX file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.exportpdf.ExportPDFToDOCX
```

#### Export a PDF file to a DOCX file (apply OCR on the PDF file)

The sample class ExportPDFToDOCXWithOCROption converts a PDF file to a DOCX file. OCR processing is also performed on the input PDF file to extract text from images in the document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.exportpdf.ExportPDFToDOCXWithOCROption
```

#### Export a PDF File To an Image Format (JPEG)

The sample class ExportPDFToJPEG converts a PDF file's pages to a list of JPEG images.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.exportpdftoimages.ExportPDFToJPEG
```

#### Export a PDF File To a Zip of Images (JPEG)

The sample class ExportPDFToJPEGZip converts a PDF file's pages to JPEG images. The resulting file is a ZIP archive containing one image per page of the source PDF file

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.exportpdftoimages.ExportPDFToJPEGZip
```

### Combine PDF Files
These samples illustrate how to combine multiple PDF files into a single PDF file.

#### Combine Multiple PDF Files

The sample class CombinePDF combines multiple PDF files into a single PDF file. The combined PDF file contains all pages
of the source files.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.combinepdf.CombinePDF 
```

#### Combine Specific Pages of Multiple PDF Files

The sample class CombinePDFWithPageRanges combines specific pages of multiple PDF files into a single PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.combinepdf.CombinePDFWithPageRanges 
```

### OCR PDF File

These samples illustrate how to apply OCR(Optical Character Recognition) to a PDF file and convert it to a searchable copy of your PDF.
The supported input format is application/pdf.

#### Convert a PDF File into a Searchable PDF File

The sample class OcrPDF converts a PDF file into a searchable PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.ocrpdf.OcrPDF
```

#### Convert a PDF File into a Searchable PDF File while keeping the original image

The sample class OcrPDFWithOptions converts a PDF file to a searchable PDF file with maximum fidelity to the original
image and default en-us locale. Refer to the documentation of OCRSupportedLocale and OCRSupportedType to see
the list of supported OCR locales and OCR types.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.ocrpdf.OcrPDFWithOptions
```

### Compress PDF File

These samples illustrate how to reduce the size of a PDF file.

#### Reduce PDF File Size

The sample class CompressPDF reduces the size of a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.compresspdf.CompressPDF
```

####  Reduce PDF File Size on the basis of Compression Level

The sample class CompressPDFWithOptions reduces the size of a PDF file on the basis of provided compression level.
Refer to the documentation of CompressionLevel to see the list of supported compression levels.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.compresspdf.CompressPDFWithOptions 
```

### Linearize PDF File

The sample illustrates how to convert a PDF file into a Linearized (also known as "web optimized") PDF file. Such PDF files are
optimized for incremental access in network environments.

#### Convert a PDF File into a Web Optimized File

The sample class LinearizePDF optimizes the PDF file for a faster Web View.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.linearizepdf.LinearizePDF
```

### Protect PDF File

These samples illustrate how to secure a PDF file with a password.

#### Convert a PDF File into a Password Protected PDF File

The sample class ProtectPDF converts a PDF file into a password protected PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.protectpdf.ProtectPDF
```

#### Protect a PDF File with an Owner Password and Permissions

The sample class ProtectPDFWithOwnerPassword secures an input PDF file with owner password and allows certain access permissions
such as copying and editing the contents, and printing of the document at low resolution.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.protectpdf.ProtectPDFWithOwnerPassword
```

### Remove Protection

The sample illustrates how to remove a password security from a PDF document.

#### Remove Protection from a PDF File

The sample class RemoveProtection removes a password security from a secured PDF document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.removeprotection.RemoveProtection
```

### Rotate Pages

The sample illustrates how to rotate pages in a PDF file.

#### Rotate Pages in PDF File

The sample class RotatePDFPages rotates specific pages in a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.rotatepages.RotatePDFPages
```

### Delete Pages

The sample illustrates how to delete pages in a PDF file.

#### Delete Pages from PDF File

The sample class DeletePDFPages removes specific pages from a PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.deletepages.DeletePDFPages
```

### Reorder Pages

The sample illustrates how to reorder the pages in a PDF file.

#### Reorder Pages in PDF File

The sample class ReorderPDFPages rearranges the pages of a PDF file according to the specified order.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.reorderpages.ReorderPDFPages
```

### Insert Pages

The sample illustrates how to insert pages in a PDF file.

#### Insert Pages into a PDF File

The sample class InsertPDFPages inserts pages of multiple PDF files into a base PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.insertpages.InsertPDFPages
```

### Replace Pages

The sample illustrates how to replace pages of a PDF file.

#### Replace PDF File Pages with Multiple PDF Files

The sample class ReplacePDFPages replaces specific pages in a PDF file with pages from multiple PDF files.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.replacepages.ReplacePDFPages
```

### Split PDF File
These samples illustrate how to split PDF file into multiple PDF files.

#### Split PDF By Number of Pages

The sample class SplitPDFByNumberOfPages splits input PDF into multiple PDF files on the basis of the maximum number
of pages each of the output files can have.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.splitpdf.SplitPDFByNumberOfPages 
```

#### Split PDF Into Number of PDF Files

The sample class SplitPDFIntoNumberOfFiles splits input PDF into multiple PDF files on the basis of the number
of documents.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.splitpdf.SplitPDFIntoNumberOfFiles 
```

#### Split PDF By Page Ranges

The sample class SplitPDFByPageRanges splits input PDF into multiple PDF files on the basis of page ranges.
Each page range corresponds to a single output file having the pages specified in the page range.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.splitpdf.SplitPDFByPageRanges 
```

### Document Merge

Adobe Document Merge Operation allows you to produce high fidelity PDF and Word documents with dynamic data inputs.
Using this operation, you can merge your JSON data with Word templates to create dynamic documents for
contracts and agreements, invoices, proposals, reports, forms, branded marketing documents and more.
To know more about document generation and document templates, please checkout the [documentation](http://www.adobe.com/go/dcdocgen_overview_doc)

#### Merge Document to DOCX

The sample class MergeDocumentToDOCX merges the Word based document template with the input JSON data to generate
the output document in the DOCX format.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.documentmerge.MergeDocumentToDOCX 
```

#### Merge Document to DOCX with Fragments

The sample class MergeDocumentToDOCXWithFragments merges the Word based document template with the input JSON data and fragments JSON to generate 
the output document in the DOCX format.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.documentmerge.MergeDocumentToDOCXWithFragments 
```

#### Merge Document to PDF

The sample class MergeDocumentToPDF merges the Word based document template with the input JSON data to generate
the output document in the PDF format.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.documentmerge.MergeDocumentToPDF 
```

### PDF Electronic Seal

These samples illustrate how to apply Electronic Seal over PDF documents. PDF Electronic Seal Operation enables the clients to perform electronic seal over the PDF documents like
agreements, invoices and more.
To know more about PDF Electronic Seal, please see the [documentation](https://developer.adobe.com/document-services/docs/overview/pdf-electronic-seal-api/).

#### Apply Electronic Seal

The sample class ElectronicSeal uses the default appearance options to apply electronic seal over the PDF document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.electronicseal.ElectronicSeal 
```

#### Electronic Seal With Custom Appearance Options

The sample class ElectronicSealWithAppearanceOptions uses the custom appearance options to apply electronic seal over the PDF document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.electronicseal.ElectronicSealWithAppearanceOptions 
```


### Extract PDF

These samples illustrate extracting content of PDF in a structured JSON format along with the renditions inside PDF.
The output of SDK extract operation is Zip package. The Zip package consists of following:

* The structuredData.json file with the extracted content & PDF element structure. See the [JSON schema](https://opensource.adobe.com/pdftools-sdk-docs/release/shared/extractJSONOutputSchema.json). Please refer the [Styling JSON schema](https://opensource.adobe.com/pdftools-sdk-docs/release/shared/extractJSONOutputSchemaStylingInfo.json) for a description of the output when the styling option is enabled.
* A renditions folder(s) containing renditions for each element type selected as input.
  The folder name is either “tables” or “figures” depending on your specified element type.
  Each folder contains renditions with filenames that correspond to the element information in the JSON file.

#### Extract Text Elements

The sample class ExtractTextInfoFromPDF.java extracts text elements from PDF document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextInfoFromPDF
```

#### Extract Text, Table Elements

The sample class ExtractTextTableInfoFromPDF extracts text, table elements from PDF document.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoFromPDF
```

#### Extract Text, Table Elements with Renditions of Table Elements

The sample class ExtractTextTableInfoWithRenditionsFromPDF extracts text, table elements along with table renditions
from PDF document. Note that the output is a zip containing the structured information along with renditions as described
in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoWithRenditionsFromPDF
```
#### Extract Text, Table Elements with Renditions of Figure, Table Elements

The sample class ExtractTextTableInfoWithFiguresTablesRenditionsFromPDF extracts text, table elements along with figure
and table element's renditions from PDF document. Note that the output is a zip containing the structured information
along with renditions as described in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoWithFiguresTablesRenditionsFromPDF
```
#### Extract Text Elements and bounding boxes for Characters present in text blocks

The sample class ExtractTextInfoWithCharBoundsFromPDF extracts text elements and bounding boxes for characters present in text blocks. Note that the output is a zip containing the structured information
along with renditions as described in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextInfoWithCharBoundsFromPDF
```

#### Extract Text, Table Elements and bounding boxes for Characters present in text blocks with Renditions of Table Elements

The sample class ExtractTextTableInfoWithCharBoundsFromPDF extracts text, table elements, bounding boxes for characters present in text blocks and
table element's renditions from PDF document. Note that the output is a zip containing the structured information
along with renditions as described in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoWithCharBoundsFromPDF
```

#### Extract Text, Table Elements with Renditions and CSV's of Table Elements

The sample class ExtractTextTableInfoWithTableStructureFromPdf extracts text, table elements, table structures as CSV and
table element's renditions from PDF document. Note that the output is a zip containing the structured information
along with renditions as described in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoWithTableStructureFromPdf
```

#### Extract Text, Table Elements with Styling information of text

The sample class ExtractTextTableInfoWithStylingFromPDF extracts text and table elements along with the styling information of the text blocks.
Note that the output is a zip containing the structured information
along with renditions as described in [section](#extract-pdf).

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.extractpdf.ExtractTextTableInfoWithStylingFromPDF
```

### PDF Properties
This sample illustrates how to fetch properties of a PDF file

#### Fetch PDF Properties

The sample class GetPDFProperties fetches the properties of an input PDF.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.pdfproperties.GetPDFProperties
```

### Autotag PDF

These samples illustrate autotagging a PDF and generating the tagged PDF and an optional report 
which contains the information about the tags the tagged document contains. This feature is in ***Beta***.

#### Generates tagged PDF from a PDF

The sample class AutotagPDF generates tagged PDF from a PDF.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.autotagpdf.AutotagPDF 
```

#### Generates tagged PDF along with a report and shift the headings in the output PDF file

The sample class AutotagPDFWithOptions generates tagged PDF with a report and shift the headings in the output PDF file.

```$xslt
mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.autotagpdf.AutotagPDFWithOptions 
```

#### Generates tagged PDF by setting options with command line arguments

The sample class AutotagPDFParamaterised generates tagged PDF by setting options through command line arguments

Here is a sample list of command line arguments and their description: </br>
--input &lt; input file path &gt; </br>
--output &lt; output file path &gt; </br>
--report { If this argument is present then the output will be generated with the report } </br>
--shift_headings { If this argument is present then the headings will be shifted in the output PDF file } </br>

```$xslt
 mvn -f pom.xml exec:java -Dexec.mainClass=com.adobe.pdfservices.operation.samples.autotagpdf.AutotagPDFParamaterised -Dexec.args="--report --shift_headings --input src/main/resources/autotagPdfInput.pdf --output output/AutotagPDFParamaterised/"
```

### Licensing

This project is licensed under the MIT License. See [LICENSE](LICENSE.md) for more information.
