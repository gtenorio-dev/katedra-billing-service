//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.04 at 07:05:27 AM ART 
//


package com.katedra.biller.app.client.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FECAERequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FECAERequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FeCabReq" type="{http://ar.gov.afip.dif.FEV1/}FECAECabRequest" minOccurs="0"/&gt;
 *         &lt;element name="FeDetReq" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfFECAEDetRequest" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAERequest", propOrder = {
    "feCabReq",
    "feDetReq"
})
public class FECAERequest {

    @XmlElement(name = "FeCabReq")
    protected FECAECabRequest feCabReq;
    @XmlElement(name = "FeDetReq")
    protected ArrayOfFECAEDetRequest feDetReq;

    /**
     * Gets the value of the feCabReq property.
     * 
     * @return
     *     possible object is
     *     {@link FECAECabRequest }
     *     
     */
    public FECAECabRequest getFeCabReq() {
        return feCabReq;
    }

    /**
     * Sets the value of the feCabReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAECabRequest }
     *     
     */
    public void setFeCabReq(FECAECabRequest value) {
        this.feCabReq = value;
    }

    /**
     * Gets the value of the feDetReq property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFECAEDetRequest }
     *     
     */
    public ArrayOfFECAEDetRequest getFeDetReq() {
        return feDetReq;
    }

    /**
     * Sets the value of the feDetReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFECAEDetRequest }
     *     
     */
    public void setFeDetReq(ArrayOfFECAEDetRequest value) {
        this.feDetReq = value;
    }

}
