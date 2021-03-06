package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.github.fluent.hibernate.annotations.FluentName;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table
public class Merchant {

    private Long pid;

    private String name;

    private Location location;

    private List<Customer> primaryCustomers = new ArrayList<Customer>();

    private List<Customer> friends = new ArrayList<Customer>();

    private Merchant firstPartner;

    private Location firstPartnerLocation;

    private List<Merchant> partners = new ArrayList<Merchant>();

    @Id
    @GeneratedValue
    @Column
    public Long getPid() {
        return pid;
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    @Embedded
    public Location getLocation() {
        return location;
    }

    /**
     * TODO check primary customers problem.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Customer> getPrimaryCustomers() {
        return primaryCustomers;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Customer> getFriends() {
        return friends;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Merchant getFirstPartner() {
        return firstPartner;
    }

    @Embedded
    @FluentName(prefix = "fpl")
    public Location getFirstPartnerLocation() {
        return firstPartnerLocation;
    }

    @ManyToMany
    public List<Merchant> getPartners() {
        return partners;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPrimaryCustomers(List<Customer> primaryCustomers) {
        this.primaryCustomers = primaryCustomers;
    }

    public void setFriends(List<Customer> friends) {
        this.friends = friends;
    }

    public void setFirstPartner(Merchant firstPartner) {
        this.firstPartner = firstPartner;
    }

    public void setFirstPartnerLocation(Location firstPartnerLocation) {
        this.firstPartnerLocation = firstPartnerLocation;
    }

    public void setPartners(List<Merchant> partners) {
        this.partners = partners;
    }

    public static Merchant create(String name) {
        Merchant result = new Merchant();
        result.setName(name);
        return result;
    }

}
