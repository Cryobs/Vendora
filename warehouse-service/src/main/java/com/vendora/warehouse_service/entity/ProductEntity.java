package com.vendora.warehouse_service.entity;


import jakarta.persistence.*;

@Entity
@Table(name="Products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int number;

    public int getNumber() {
        return number;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ProductEntity(Long id, String name, String description, int number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.number = number;
    }
}
