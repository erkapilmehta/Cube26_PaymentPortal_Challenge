package com.pankaj.cube26_paymentportal.Entities;

import java.io.Serializable;

/**
 * Created by Pankaj on 13/03/16.
 */
public class PaymentGateways implements Serializable {
    String id;
    String name;
    String image;
    String description;
    String branding;
    String rating;
    String currencies;
    String setup_fee;
    String transaction_fees;
    String how_to_document;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranding() {
        return branding;
    }

    public void setBranding(String branding) {
        this.branding = branding;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCurrencies() {
        return currencies;
    }

    public void setCurrencies(String currencies) {
        this.currencies = currencies;
    }

    public String getSetup_fee() {
        return setup_fee;
    }

    public void setSetup_fee(String setup_fee) {
        this.setup_fee = setup_fee;
    }

    public String getTransaction_fees() {
        return transaction_fees;
    }

    public void setTransaction_fees(String transaction_fees) {
        this.transaction_fees = transaction_fees;
    }

    public String getHow_to_document() {
        return how_to_document;
    }

    public void setHow_to_document(String how_to_document) {
        this.how_to_document = how_to_document;
    }
}
