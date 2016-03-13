package com.pankaj.cube26_paymentportal.Entities;

import java.io.Serializable;

/**
 * Created by Pankaj on 13/03/16.
 */
public class Likes implements Serializable {
    String name;
    String totalCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}
