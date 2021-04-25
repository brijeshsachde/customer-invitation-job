package com.brijeshsachde.customer.invitation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Customer {

    @JsonProperty("user_id")
    private long id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private double latitude;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private double longitude;

    public Customer() {
    }

    public Customer(long id, String name, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = Double.valueOf(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.valueOf(longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Double.compare(customer.latitude, latitude) == 0 && Double.compare(customer.longitude, longitude) == 0 && name.equals(customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}