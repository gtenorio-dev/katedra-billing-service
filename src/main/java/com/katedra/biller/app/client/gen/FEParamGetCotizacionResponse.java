//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.02.13 at 07:46:13 PM ART 
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
 *         &lt;element name="FEParamGetCotizacionResult" type="{http://ar.gov.afip.dif.FEV1/}FECotizacionResponse" minOccurs="0"/&gt;
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
    "feParamGetCotizacionResult"
})
@XmlRootElement(name = "FEParamGetCotizacionResponse")
public class FEParamGetCotizacionResponse {

    @XmlElement(name = "FEParamGetCotizacionResult")
    protected FECotizacionResponse feParamGetCotizacionResult;

    /**
     * Gets the value of the feParamGetCotizacionResult property.
     * 
     * @return
     *     possible object is
     *     {@link FECotizacionResponse }
     *     
     */
    public FECotizacionResponse getFEParamGetCotizacionResult() {
        return feParamGetCotizacionResult;
    }

    /**
     * Sets the value of the feParamGetCotizacionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FECotizacionResponse }
     *     
     */
    public void setFEParamGetCotizacionResult(FECotizacionResponse value) {
        this.feParamGetCotizacionResult = value;
    }

}
