package com.katedra.biller.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.spring.boot.client.MatrixToImageWriter;
import com.katedra.biller.app.client.gen.FECompConsResponse;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.model.QRData;
import org.apache.axis.encoding.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class QRCodeGenerator {

    public static ByteArrayOutputStream generateQRCode(
            FECompConsResponse comprobante, AccountEntity account, String qrUrl) throws WriterException, IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat qrDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        QRData qrData = new QRData();
        qrData.setVer(1); // Default Version --> 1
        qrData.setFecha(qrDateFormat.format(dateFormat.parse(comprobante.getCbteFch())));
        qrData.setCuit(account.getCuit());
        qrData.setPtoVta(comprobante.getPtoVta());
        qrData.setTipoCmp(comprobante.getCbteTipo());
        qrData.setNroCmp(comprobante.getCbteDesde());
        qrData.setImporte(comprobante.getImpTotal());
        qrData.setMoneda(comprobante.getMonId());
        qrData.setCtz(1); // Cotización en pesos argentinos de la moneda utilizada (1 cuando la moneda sea pesos)
        qrData.setTipoDocRec(comprobante.getDocTipo());
        qrData.setNroDocRec(comprobante.getDocNro());
        qrData.setTipoCodAut("CAE".equals(comprobante.getEmisionTipo())? "E" : "A");
        qrData.setCodAut(Long.valueOf(comprobante.getCodAutorizacion()));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(qrData);

        BitMatrix bitMatrix = new QRCodeWriter().encode(
                qrUrl.concat(Base64.encode(json.getBytes())),
                BarcodeFormat.QR_CODE, 100, 100);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", output);
        return output;
    }

}
