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
 * <p>Java class for CbteAsoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CbteAsoc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Tipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Nro" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Cuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CbteFch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CbteAsoc", propOrder = {
    "tipo",
    "ptoVta",
    "nro",
    "cuit",
    "cbteFch"
})
public class CbteAsoc {

    @XmlElement(name = "Tipo")
    protected int tipo;
    @XmlElement(name = "PtoVta")
    protected int ptoVta;
    @XmlElement(name = "Nro")
    protected long nro;
    @XmlElement(name = "Cuit")
    protected String cuit;
    @XmlElement(name = "CbteFch")
    protected String cbteFch;

    /**
     * Gets the value of the tipo property.
     * 
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     */
    public void setTipo(int value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the ptoVta property.
     * 
     */
    public int getPtoVta() {
        return ptoVta;
    }

    /**
     * Sets the value of the ptoVta property.
     * 
     */
    public void setPtoVta(int value) {
        this.ptoVta = value;
    }

    /**
     * Gets the value of the nro property.
     * 
     */
    public long getNro() {
        return nro;
    }

    /**
     * Sets the value of the nro property.
     * 
     */
    public void setNro(long value) {
        this.nro = value;
    }

    /**
     * Gets the value of the cuit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuit() {
        return cuit;
    }

    /**
     * Sets the value of the cuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuit(String value) {
        this.cuit = value;
    }

    /**
     * Gets the value of the cbteFch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCbteFch() {
        return cbteFch;
    }

    /**
     * Sets the value of the cbteFch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCbteFch(String value) {
        this.cbteFch = value;
    }

}
