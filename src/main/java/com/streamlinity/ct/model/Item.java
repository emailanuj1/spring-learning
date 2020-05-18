package com.streamlinity.ct.model;

/*
 * Generated class that matches the structure of the JSON coming from the Streamlinity Restaurant Menu Service.
 *
 * This is a convenience class provided for your use.
 *
 * Do not edit this
 */

public class Item {
    private float id;
    private String name;
    private String description;
    private String short_name;
    private String category_short_name;
    private String small_portion_name;
    private String large_portion_name;
    private float price_small;
    private float price_large;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getSmall_portion_name() {
        return small_portion_name;
    }

    public String getLarge_portion_name() {
        return large_portion_name;
    }

    public float getPrice_small() {
        return price_small;
    }

    public float getPrice_large() {
        return price_large;
    }

    public String getCategory_short_name() {
        return category_short_name;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setSmall_portion_name(String small_portion_name) {
        this.small_portion_name = small_portion_name;
    }

    public void setLarge_portion_name(String large_portion_name) {
        this.large_portion_name = large_portion_name;
    }

    public void setPrice_small(float price_small) {
        this.price_small = price_small;
    }

    public void setPrice_large(float price_large) {
        this.price_large = price_large;
    }

    public void setCategory_short_name(String category_short_name) {
        this.category_short_name = category_short_name;
    }
}
