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
 * <p>Java class for Comprador complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Comprador"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="DocNro" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Porcentaje" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Comprador", propOrder = {
    "docTipo",
    "docNro",
    "porcentaje"
})
public class Comprador {

    @XmlElement(name = "DocTipo")
    protected int docTipo;
    @XmlElement(name = "DocNro")
    protected long docNro;
    @XmlElement(name = "Porcentaje")
    protected double porcentaje;

    /**
     * Gets the value of the docTipo property.
     * 
     */
    public int getDocTipo() {
        return docTipo;
    }

    /**
     * Sets the value of the docTipo property.
     * 
     */
    public void setDocTipo(int value) {
        this.docTipo = value;
    }

    /**
     * Gets the value of the docNro property.
     * 
     */
    public long getDocNro() {
        return docNro;
    }

    /**
     * Sets the value of the docNro property.
     * 
     */
    public void setDocNro(long value) {
        this.docNro = value;
    }

    /**
     * Gets the value of the porcentaje property.
     * 
     */
    public double getPorcentaje() {
        return porcentaje;
    }

    /**
     * Sets the value of the porcentaje property.
     * 
     */
    public void setPorcentaje(double value) {
        this.porcentaje = value;
    }

}
