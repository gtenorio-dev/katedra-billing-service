package com.katedra.biller.app.utils;

import com.katedra.biller.app.client.gen.FECompConsResponse;
import com.katedra.biller.app.entity.AccountEntity;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PDFGenerator {

    public static ByteArrayInputStream generate(AccountEntity account, FECompConsResponse comprobante) throws IOException, ParseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // ----- Fonts -----
        Font h1S = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        h1S.setSize(16);
        Font h4B = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        h4B.setSize(10);
        Font h4 = FontFactory.getFont(FontFactory.HELVETICA);
        h4.setSize(10);

        // ----- Header Left -----
        Cell t1Left = noBorderCell();
        t1Left.setHorizontalAlignment(HorizontalAlignment.CENTER);
        t1Left.setVerticalAlignment(VerticalAlignment.CENTER);
        t1Left.add(new Paragraph("KATEDRA", h1S));

        Table accountInfoLeftT = noBorderTable(2);

        Cell cRSocial = noBorderCell();
        cRSocial.add(new Paragraph("Razon Social: ", h4B));
        Cell cRSocialValue = noBorderCell();
        cRSocialValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cRSocial);
        accountInfoLeftT.addCell(cRSocialValue);

        Cell cDomicilio = noBorderCell();
        cDomicilio.add(new Paragraph("Domicilio Comercial: ", h4B));
        Cell cDomicilioValue = noBorderCell();
        cDomicilioValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cDomicilio);
        accountInfoLeftT.addCell(cDomicilioValue);

        Cell cCondIVA = noBorderCell();
        cCondIVA.add(new Paragraph("Condicion frente al IVA: ", h4B));
        Cell cCondIVAValue = noBorderCell();
        cCondIVAValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cCondIVA);
        accountInfoLeftT.addCell(cCondIVAValue);

        t1Left.add(accountInfoLeftT);

        // ----- Header Right -----
        Cell t1Right = noBorderCell();
        t1Right.add(new Paragraph("Factura C", h1S));

        Table t1C2T1 = noBorderTable(2);
        t1C2T1.setWidth(100);
        t1C2T1.setWidths(new int[]{30, 70});

        Cell cCompNro = noBorderCell();
        cCompNro.add(new Paragraph("Comp Nro: ", h4B));

        Cell cCompNroValue = noBorderCell();
        cCompNroValue.add(new Paragraph(Long.toString(comprobante.getCbteDesde()), h4));

        t1C2T1.addCell(cCompNro);
        t1C2T1.addCell(cCompNroValue);

        Cell cCbteFecha = noBorderCell();
        cCbteFecha.add(new Paragraph("Fecha: ", h4B));

        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        Cell cCbteFechaValue = noBorderCell();
        cCbteFechaValue.add(new Paragraph(format2.format(format1.parse(comprobante.getCbteFch())), h4));

        t1C2T1.addCell(cCbteFecha);
        t1C2T1.addCell(cCbteFechaValue);

        Cell cCuit = noBorderCell();
        cCuit.add(new Paragraph("CUIT: ", h4B));
        Cell cCuitValue = noBorderCell();
        cCuitValue.add(new Paragraph(Long.toString(account.getCuit()), h4));

        t1C2T1.addCell(cCuit);
        t1C2T1.addCell(cCuitValue);

        Cell cPtoVenta = noBorderCell();
        cPtoVenta.add(new Paragraph("Punto de Venta: ", h4B));
        Cell cPtoVentaValue = noBorderCell();
        cPtoVentaValue.add(new Paragraph(Long.toString(comprobante.getPtoVta()), h4));

        t1C2T1.addCell(cPtoVenta);
        t1C2T1.addCell(cPtoVentaValue);

        t1Right.add(t1C2T1);

        // ----- Header -----
        Table t1 = noBorderTable(2);
        t1.setWidth(110);
        t1.setWidths(new int[]{50, 50});
        t1.setBorder(15);
        t1.setBorderWidth(1);
        t1.setPadding(1);
        t1.addCell(t1Left);
        t1.addCell(t1Right);

        // ----- Dividers -----
        Table emptyDivider = noBorderTable(1);
        emptyDivider.addCell(noBorderCell());
        emptyDivider.setPadding(5F);

        Table greyDivider = new Table(1);
        greyDivider.setBorderColor(Color.GRAY);
        greyDivider.addCell(new Cell());
        greyDivider.setWidth(100F);
        greyDivider.setPadding(1F);
        greyDivider.setBackgroundColor(Color.GRAY);

        // ----- Account Info -----
//        Table accountInfo = noBorderTable(2);
//        accountInfo.setBorder(15);
//        accountInfo.setWidth(110F);
//        accountInfo.setPadding(2F);
//
//        // LEFT
//        Table accountInfoLeftT = noBorderTable(2);
//        accountInfoLeftT.setWidth(100);
//        accountInfoLeftT.setCellsFitPage(true);
//
//        Cell cRSocial = noBorderCell();
//        cRSocial.add(new Paragraph("Razón Social: ", h4B));
//        Cell cRSocialValue = noBorderCell();
//        cRSocialValue.add(new Paragraph("TODO", h4));
//
//        accountInfoLeftT.addCell(cRSocial);
//        accountInfoLeftT.addCell(cRSocialValue);
//
//        Cell cDomicilio = noBorderCell();
//        cDomicilio.add(new Paragraph("Domicilio Comercial: ", h4B));
//        Cell cDomicilioValue = noBorderCell();
//        cDomicilioValue.add(new Paragraph("TODO", h4));
//
//        accountInfoLeftT.addCell(cDomicilio);
//        accountInfoLeftT.addCell(cDomicilioValue);
//
//        Cell cCondIVA = noBorderCell();
//        cCondIVA.add(new Paragraph("Condicion frente al IVA: ", h4B));
//        Cell cCondIVAValue = noBorderCell();
//        cCondIVAValue.add(new Paragraph("TODO", h4));
//
//        accountInfoLeftT.addCell(cCondIVA);
//        accountInfoLeftT.addCell(cCondIVAValue);
//
//        // RIGHT
//
//        Table accountInfoRightT = noBorderTable(2);
//        accountInfoRightT.setWidth(100);
//
//        Cell cCuit = noBorderCell();
//        cCuit.add(new Paragraph("CUIT: ", h4B));
//        Cell cCuitValue = noBorderCell();
//        cCuitValue.add(new Paragraph(Long.toString(account.getCuit()), h4));
//
//        accountInfoRightT.addCell(cCuit);
//        accountInfoRightT.addCell(cCuitValue);
//
//        Cell cPtoVenta = noBorderCell();
//        cPtoVenta.add(new Paragraph("Punto de Venta: ", h4B));
//        Cell cPtoVentaValue = noBorderCell();
//        cPtoVentaValue.add(new Paragraph(Long.toString(comprobante.getPtoVta()), h4));
//
//        accountInfoRightT.addCell(cPtoVenta);
//        accountInfoRightT.addCell(cPtoVentaValue);
//
//
//        Cell accountInfoLeft = noBorderCell();
//        accountInfoLeft.add(accountInfoLeftT);
//
//        Cell accountInfoRight = noBorderCell();
//        accountInfoRight.add(accountInfoRightT);
//
//        accountInfo.addCell(accountInfoLeft);
//        accountInfo.addCell(accountInfoRight);


        // ----- Client Info -----



        // ----- Products -----

        // ----- Total -----

        // ----- CAE -----

        document.add(t1);
        document.add(emptyDivider);
        document.add(greyDivider);
        document.add(emptyDivider);
//        document.add(accountInfo);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }


    private static Cell noBorderCell() {
        Cell cell = new Cell();
        cell.setBorderWidth(Rectangle.NO_BORDER);
        return cell;
    }

    private static Table noBorderTable(int columns) {
        Table table = new Table(columns);
        table.setBorder(Rectangle.NO_BORDER);
        return table;
    }
}
