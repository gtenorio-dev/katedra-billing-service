//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.02.10 at 10:42:58 PM ART 
//


package com.katedra.biller.app.client.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FECAESolicitarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fecaeSolicitarResult"
})
@XmlRootElement(name = "FECAESolicitarResponse")
public class FECAESolicitarResponse {

    @XmlElement(name = "FECAESolicitarResult")
    protected FECAEResponse fecaeSolicitarResult;

    /**
     * Gets the value of the fecaeSolicitarResult property.
     * 
     * @return
     *     possible object is
     *     {@link FECAEResponse }
     *     
     */
    public FECAEResponse getFECAESolicitarResult() {
        return fecaeSolicitarResult;
    }

    /**
     * Sets the value of the fecaeSolicitarResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEResponse }
     *     
     */
    public void setFECAESolicitarResult(FECAEResponse value) {
        this.fecaeSolicitarResult = value;
    }

}
