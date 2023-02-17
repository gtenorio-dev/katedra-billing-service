# katedra-billing-service
AFIP WSFEV1 - Katedra Billing

## Documentacion AFIP

- https://www.afip.gob.ar/ws/documentacion/arquitectura-general.asp
- https://www.afip.gob.ar/ws/documentacion/ws-factura-electronica.asp
- https://www.afip.gob.ar/ws/WSFEV1/Manual_Desarrollador_COMPG_v2_20_transferencias-1.pdf
- https://www.afip.gob.ar/fe/ayuda/documentos/wsfev1-COMPG.pdf
- https://www.afip.gob.ar/ws/WSAA/WSAAmanualDev.pdf
- https://www.afip.gob.ar/ws/WSASS/WSASS_manual.pdf
- https://www.afip.gob.ar/ws/WSASS/html/crearcertificado.html

## Generación de Certificados Test

### 1. Generar una key

openssl genrsa -out privkey003.key 2048

### 2. Generar certificado para subir a AFIP

openssl req -new -key privkey003.key -subj "/C=AR/O=GABIL EDGAR TENORIO DE LA CRUZ/CN=my cert test 3/serialNumber=CUIT 20940052301" -out gtenorioAFIPTest3.csr

### 3. Abrir y copiar el certificado generado para subir a AFIP

cat gtenorioAFIPTest3.csr

### 4. Ingresar el certificado a AFIP

### 5. Guardar el nuevo certificado emitido por AFIP en un archivo con extension .pem

afipCert003.pem

### 6. Dar acceso al web service de facturacion mediante la interfaz de AFIP

(Ej de respuesta existosa)

OK. Autorización fue creada (CUITCOMPUTADOR=20940052301, ALIASCOMPUTADOR=facturaElectronicaTest3, CUITREPRESENTADO=20940052301, SERVICIO=ws://wsfe, CUITAUTORIZANTE=20940052301).


### 7. Generar archivo .p12 (este se usa para poder autenticarse en los servidores de AFIP)

openssl pkcs12 -export -in afipCert003.pem -inkey privkey003.key -name "gtest" -out certificadoTest003.p12
password: test001

### 8. Importar el archivo .p12 al proyecto

### 9. Revisar application.properties para ver la configuracion