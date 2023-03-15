package com.katedra.biller.app.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import java.awt.*;

//https://stackoverflow.com/questions/15918382/how-to-make-a-footer-in-generating-a-pdf-file-using-an-itextpdf
public class HeaderAndFooter extends PdfPageEventHelper {

    private String name = "";


    protected Phrase footer;
    protected Phrase header;

    /*
     * Font for header and footer part.
     */
    private static Font headerFont = new Font(Font.COURIER, 9,
            Font.NORMAL, Color.blue);

    private static Font footerFont = new Font(Font.TIMES_ROMAN, 9,
            Font.BOLD, Color.blue);


    /*
     * constructor
     */
    public HeaderAndFooter(String name, Cell cell) {
        super();

        this.name = name;


        header = new Phrase("***** Header *****");
        footer = new Phrase("**** Footer ****");
    }


    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();

        //header content
        String headerContent = "Name: " + name;

        //header content
        String footerContent = headerContent;
        /*
         * Header
         */
        Phrase testt = new Phrase(headerContent, headerFont);
//        testt.add(new Cell("asdasdasd"));

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, testt,
                document.leftMargin() - 1, document.top() + 30, 0);

        /*
         * Foooter
         */
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, new Phrase(),
                document.right(), document.bottom(), 0);

    }

}
