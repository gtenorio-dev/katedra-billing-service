package com.katedra.biller.app.utils;

import com.google.zxing.WriterException;
import com.katedra.biller.app.client.gen.FECompConsResponse;
import com.katedra.biller.app.dto.BillDTO;
import com.katedra.biller.app.dto.ProductDTO;
import com.katedra.biller.app.entity.AccountEntity;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PDFGenerator {

    private static final DecimalFormat df = new DecimalFormat("000,000.00");

    public static ByteArrayInputStream generate(AccountEntity account, BillDTO billDTO,
                                                FECompConsResponse comprobante, String qrUrl) throws IOException, ParseException, WriterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        // ----- Colors -----
        Color transparent = new Color(0F,0F,0F,0F);

        // ----- Fonts -----
        Font h1B = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        h1B.setSize(16);
        Font h1 = FontFactory.getFont(FontFactory.HELVETICA);
        h1.setSize(16);
        Font h4B = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        h4B.setSize(10);
        Font h4 = FontFactory.getFont(FontFactory.HELVETICA);
        h4.setSize(10);

        // ----- Date Fromats -----
        SimpleDateFormat dateAfipFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat datePDFFormat = new SimpleDateFormat("dd/MM/yyyy");

        // ----- Logo -----
        Image logo = Image.getInstance(PDFGenerator.class.getClassLoader().getResource("img/logo.png"));
        float logoScale = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / logo.getWidth()) * 10;
        logo.scalePercent(logoScale);

        Cell cLogo = noBorderCell();
        cLogo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cLogo.add(logo);

        // ----- Title -----
        Cell title = noBorderCell();
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        title.add(new Paragraph("KATEDRA", h1B));

        // ----- Header Left -----
        Cell t1Left = noBorderCell();

        Table accountInfoLeftT = noBorderTable(2);
        accountInfoLeftT.setWidth(100);
        accountInfoLeftT.setBorder(15);

        Cell cRSocial = noBorderCell(2);
        cRSocial.add(new Paragraph("Razon Social: ", h4B));
        Cell cRSocialValue = noBorderCell(2);
        cRSocialValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cRSocial);
        accountInfoLeftT.addCell(cRSocialValue);

        Cell cDomicilio = noBorderCell(2);
        cDomicilio.add(new Paragraph("Domicilio Comercial: ", h4B));
        Cell cDomicilioValue = noBorderCell(2);
        cDomicilioValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cDomicilio);
        accountInfoLeftT.addCell(cDomicilioValue);

        Cell cCondIVA = noBorderCell(2);
        cCondIVA.add(new Paragraph("Condicion frente al IVA: ", h4B));
        Cell cCondIVAValue = noBorderCell(2);
        cCondIVAValue.add(new Paragraph("TODO", h4));

        accountInfoLeftT.addCell(cCondIVA);
        accountInfoLeftT.addCell(cCondIVAValue);

        t1Left.add(accountInfoLeftT);

        // ----- Header Right -----
        Cell t1Right = noBorderCell();

        Table t1C2T1 = noBorderTable(2);
        t1C2T1.setWidth(100);
        t1C2T1.setWidths(new int[]{40, 60});

        Cell cCompNro = noBorderCell(2);
        cCompNro.add(new Paragraph("Comp Nro: ", h4B));

        Cell cCompNroValue = noBorderCell(2);
        cCompNroValue.add(new Paragraph(Long.toString(comprobante.getCbteDesde()), h4));

        t1C2T1.addCell(cCompNro);
        t1C2T1.addCell(cCompNroValue);

        Cell cCbteFecha = noBorderCell(2);
        cCbteFecha.add(new Paragraph("Fecha: ", h4B));

        Cell cCbteFechaValue = noBorderCell(2);
        cCbteFechaValue.add(new Paragraph(datePDFFormat.format(dateAfipFormat.parse(comprobante.getCbteFch())), h4));

        t1C2T1.addCell(cCbteFecha);
        t1C2T1.addCell(cCbteFechaValue);

        Cell cCuit = noBorderCell(2);
        cCuit.add(new Paragraph("CUIT: ", h4B));
        Cell cCuitValue = noBorderCell(2);
        cCuitValue.add(new Paragraph(Long.toString(account.getCuit()), h4));

        t1C2T1.addCell(cCuit);
        t1C2T1.addCell(cCuitValue);

        Cell cPtoVenta = noBorderCell(2);
        cPtoVenta.add(new Paragraph("Punto de Venta: ", h4B));
        Cell cPtoVentaValue = noBorderCell(2);
        cPtoVentaValue.add(new Paragraph(Long.toString(comprobante.getPtoVta()), h4));

        t1C2T1.addCell(cPtoVenta);
        t1C2T1.addCell(cPtoVentaValue);

        t1Right.add(t1C2T1);

        // ----- Header -----
        Table accountInfo = noBorderTable(2);
        accountInfo.setWidth(100);
        accountInfo.setWidths(new int[]{60, 40});
//        accountInfo.setBorder(15);
//        accountInfo.setBorderWidth(1);
//        accountInfo.setPadding(1);

        accountInfo.addCell(cLogo);
        accountInfo.addCell(noBorderCell(0));

        accountInfo.addCell(title);
        accountInfo.addCell(noBorderCell(new Paragraph("Factura C", h1B), 2));

        accountInfo.addCell(t1Left);
        accountInfo.addCell(t1Right);

        // ----- Dividers -----
        Table emptyDivider = noBorderTable(1);
        emptyDivider.addCell(noBorderCell());
        emptyDivider.setPadding(2);

        Table greyDivider = new Table(1);
        greyDivider.setBorderColor(Color.GRAY);
        greyDivider.addCell(new Cell());
        greyDivider.setWidth(100);
//        greyDivider.setPadding(1F);
        greyDivider.setBackgroundColor(Color.GRAY);

        // ----- Service Info -----

        Table serviceInfo = noBorderTable(3);
        serviceInfo.setWidth(100);
        serviceInfo.setWidths(new int[]{40,20,40});

        String dedse = "Periodo Facturado Desde: ".concat(datePDFFormat.format(dateAfipFormat.parse(comprobante.getFchServDesde())));
        String hasta = "Hasta: ".concat(datePDFFormat.format(dateAfipFormat.parse(comprobante.getFchServHasta())));
        String vtoPago = "Fecha de Vto. para el pago: ".concat(datePDFFormat.format(dateAfipFormat.parse(comprobante.getFchVtoPago())));
        serviceInfo.addCell(noBorderCell(new Paragraph(dedse, h4),15));
        serviceInfo.addCell(noBorderCell(new Paragraph(hasta, h4), 15));
        serviceInfo.addCell(noBorderCell(new Paragraph(vtoPago, h4), 15));

        // ----- Client Info -----
        Table clientInfo = new Table(2);
        clientInfo.setWidth(100);
        clientInfo.setBorderColor(Color.GRAY);

        clientInfo.addCell(noBorderCell(new Paragraph("DNI: ".concat(Long.toString(comprobante.getDocNro())), h4)));
        clientInfo.addCell(noBorderCell(new Paragraph("Nombre y Apellido:".concat(billDTO.getNombreYApellido()), h4)));
        clientInfo.addCell(noBorderCell(new Paragraph("Condición frente al IVA: Consumidor Final", h4)));
        clientInfo.addCell(noBorderCell());
        clientInfo.addCell(noBorderCell(new Paragraph("Forma de Pago: ".concat(billDTO.getFormaDePago()), h4)));
        clientInfo.addCell(noBorderCell());
        clientInfo.addCell(noBorderCell(5));


        // ----- Products -----

        Table products = new Table(4);
        products.setWidth(100);
        products.setBorderColor(Color.GRAY);
        products.setWidths(new int[]{45,15,20,20});

        Cell descriptionTitle = noBorderCell(new Paragraph("Producto/Servicio", h4B),10);
        descriptionTitle.setBackgroundColor(Color.GRAY);
        products.addCell(descriptionTitle);

        Cell quantityTitle = noBorderCell(new Paragraph("Cantidad", h4B),10);
        quantityTitle.setBackgroundColor(Color.GRAY);
        products.addCell(quantityTitle);

        Cell productPriceTitle = noBorderCell(new Paragraph("Precio Unitario", h4B),10);
        productPriceTitle.setBackgroundColor(Color.GRAY);
        products.addCell(productPriceTitle);

        Cell totalTitle = noBorderCell(new Paragraph("Subtotal", h4B),10);
        totalTitle.setBackgroundColor(Color.GRAY);
        products.addCell(totalTitle);

        // ----- Products Item -----

        for (ProductDTO item : billDTO.getProductos()) {
            products.addCell(noBorderCell(new Paragraph(item.getNombre(), h4),5));
            products.addCell(noBorderCell(new Paragraph(Integer.toString(item.getCantidad()), h4),5));
            products.addCell(noBorderCell(new Paragraph(
                    String.format(Locale.ITALY,"%,.2f", item.getPrecioUnitario()), h4),5));
            products.addCell(noBorderCell(new Paragraph(
                    String.format(Locale.ITALY,"%,.2f", item.getTotal()), h4),5));
        }
        products.addCell(noBorderCell(10));


        // ----- Total -----
        Table totalInfo = new Table(2);
        totalInfo.setWidth(100);
        totalInfo.setBorderColor(Color.GRAY);
        totalInfo.setWidths(new int[]{60,40});

        totalInfo.addCell(noBorderCell(25));
        totalInfo.addCell(noBorderCell(25));
        totalInfo.addCell(noBorderCell());

        Table totalAmounts = noBorderTable(2);

        Cell subtotalCell = noBorderCell(new Paragraph("Subtotal: $", h4B));
        subtotalCell.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        Cell subtotalValueCell = noBorderCell(new Paragraph(String.format(Locale.ITALY,"%,.2f", comprobante.getImpTotal()), h4B));
        subtotalValueCell.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        totalAmounts.addCell(subtotalCell);
        totalAmounts.addCell(subtotalValueCell);

        Cell total = noBorderCell(new Paragraph("Importe Total: $", h4B));
        total.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        Cell totalValue = noBorderCell(new Paragraph(String.format(Locale.ITALY,"%,.2f", comprobante.getImpTotal()), h4B));
        totalValue.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        totalAmounts.addCell(total);
        totalAmounts.addCell(totalValue);

        totalInfo.addCell(noBorderCell(totalAmounts));

        totalInfo.addCell(noBorderCell(15));
        totalInfo.addCell(noBorderCell(15));


        // ----- Footer - CAE -----
        Table caeInfo = new Table(2);
        caeInfo.setWidth(100);
        caeInfo.setBorderColor(Color.GRAY);
        caeInfo.setWidths(new int[]{60,40});

        caeInfo.addCell(noBorderCell(Image.getInstance(QRCodeGenerator.generateQRCode(comprobante, account, qrUrl).toByteArray())));

        Cell cae = noBorderCell(new Paragraph("C.A.E. Nro: ".concat(comprobante.getCodAutorizacion()), h4B), 20);
        cae.add(new Paragraph("Fecha Vto. de CAE: ".concat(datePDFFormat.format(dateAfipFormat.parse(comprobante.getFchVto()))), h4B));
        caeInfo.addCell(cae);


        // ----- Layout -----
        document.open();
        document.add(accountInfo);
        document.add(emptyDivider);
        document.add(greyDivider);
        document.add(serviceInfo);
        document.add(emptyDivider);
        document.add(greyDivider);
        document.add(clientInfo);
        document.add(emptyDivider);
        document.add(products);
        document.add(totalInfo);
        document.add(emptyDivider);
        document.add(caeInfo);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static Cell noBorderCell() {
        Cell cell = new Cell();
        cell.setBorderWidth(5);
        Color transparent = new Color (0F,0F,0F,0F);
        cell.setBorderColor(transparent);
        cell.setUseBorderPadding(true);
        return cell;
    }

    private static Cell noBorderCell(int padding) {
        Cell cell = new Cell();
        cell.setBorderWidth(padding);
        Color transparent = new Color (0F,0F,0F,0F);
        cell.setBorderColor(transparent);
        cell.setUseBorderPadding(true);
        return cell;
    }

    private static Cell noBorderCell(Element element) {
        Cell cell = new Cell(element);
        cell.setBorderWidth(5);
        Color transparent = new Color (0F,0F,0F,0F);
        cell.setBorderColor(transparent);
        cell.setUseBorderPadding(true);
        return cell;
    }

    private static Cell noBorderCell(Element element, int padding) {
        Cell cell = new Cell(element);
        cell.setBorderWidth(padding);
        Color transparent = new Color (0F,0F,0F,0F);
        cell.setBorderColor(transparent);
        cell.setUseBorderPadding(true);
        return cell;
    }

    private static Table noBorderTable(int columns) {
        Table table = new Table(columns);
        table.setBorder(Rectangle.NO_BORDER);
        return table;
    }

}
