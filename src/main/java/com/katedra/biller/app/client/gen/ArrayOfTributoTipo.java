//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.04 at 03:48:38 AM ART 
//


package com.katedra.biller.app.client.gen;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTributoTipo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTributoTipo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TributoTipo" type="{http://ar.gov.afip.dif.FEV1/}TributoTipo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTributoTipo", propOrder = {
    "tributoTipo"
})
public class ArrayOfTributoTipo {

    @XmlElement(name = "TributoTipo", nillable = true)
    protected List<TributoTipo> tributoTipo;

    /**
     * Gets the value of the tributoTipo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tributoTipo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTributoTipo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TributoTipo }
     * 
     * 
     */
    public List<TributoTipo> getTributoTipo() {
        if (tributoTipo == null) {
            tributoTipo = new ArrayList<TributoTipo>();
        }
        return this.tributoTipo;
    }

}
