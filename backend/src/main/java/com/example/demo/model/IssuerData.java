package com.example.demo.model;

//import org.bouncycastle.asn1.x500.X500Name;

import org.bouncycastle.asn1.x500.X500Name;

import javax.persistence.*;
import java.security.PrivateKey;

@Entity
public class IssuerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProba() {
        return proba;
    }

    public void setProba(String proba) {
        this.proba = proba;
    }

    @Column
    private String proba;

    /*private X500Name x500name;

    private PrivateKey privateKey;*/

    public IssuerData() {
    }

  /* public IssuerData(PrivateKey privateKey, X500Name x500name) {
        this.privateKey = privateKey;
        this.x500name = x500name;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }*/

}
