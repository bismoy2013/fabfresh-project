package com.fabfresh.api.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "people")
@NamedQueries({
        @NamedQuery(
                name = "com.fabfresh.api.core.Person.findAll",
                query = "SELECT p FROM Person p"
        )
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;
    
    @Column(name = "phoneno", nullable = false)
    private int phoneNumber;
    
    @JsonProperty
    private Date pickUpDate ;
    
    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }
    
    public Date getPickUpDate() {
        return pickUpDate;
      }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        final Person that = (Person) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.address, that.address) &&
                Objects.equals(this.phoneNumber, that.phoneNumber) &&
                Objects.equals(this.email, that.email) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pickUpDate, address, phoneNumber, email);
    }
}
