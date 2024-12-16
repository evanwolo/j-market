package org.example.marketsystem.model;

import jakarta.persistence.*;

@Entity
public class MarketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    public MarketItem(String name, String description) {
        this.id = 0L;
        this.name = name;
        this.description = description;
    }

    public MarketItem() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "MarketItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
