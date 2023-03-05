package com.katedra.biller.app.utils;

//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
import com.katedra.biller.app.client.gen.FECompConsResponse;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.annotation.Documented;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFGenerator {

    public static ByteArrayInputStream generate(String fileName, FECompConsResponse comprobante) throws IOException {
        Path tempFile = null;
        tempFile = Files.createTempFile(fileName, ".pdf");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();

        return new ByteArrayInputStream(out.toByteArray());

//        Path tempFile = null;
//        try {
//            // Create an temporary file
//            tempFile = Files.createTempFile(fileName, ".pdf");
////            Path temp = Files.createTempFile(fileName, ".pdf");
//            System.out.println("Temp file : " + tempFile);
//
//            PdfWriter pdfWriter = new PdfWriter(tempFile.toString());
//            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//            pdfDocument.setDefaultPageSize(PageSize.A4);
//
//            Document document = new Document(pdfDocument);
//
//            document.add(new Paragraph("Hello World!"));
//            document.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "TODO";
    }

}
