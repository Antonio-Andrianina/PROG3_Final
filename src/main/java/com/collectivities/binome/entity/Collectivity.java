package com.collectivities.binome.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Collectivity {
    private String id;
    private String name;
    private LocalDate creationDate;
    private boolean federationApproval;
    private City city;
    private AgriculturalSpecialty agriculturalSpecialty;
    private List<CollectivityMandate> mandates;

    public Collectivity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public boolean isFederationApproval() { return federationApproval; }
    public void setFederationApproval(boolean federationApproval) { this.federationApproval = federationApproval; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public AgriculturalSpecialty getAgriculturalSpecialty() { return agriculturalSpecialty; }
    public void setAgriculturalSpecialty(AgriculturalSpecialty agriculturalSpecialty) { this.agriculturalSpecialty = agriculturalSpecialty; }

    public List<CollectivityMandate> getMandates() { return mandates; }
    public void setMandates(List<CollectivityMandate> mandates) { this.mandates = mandates; }
}
