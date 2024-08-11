package com.FB_APP.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;

    @ElementCollection
    @CollectionTable(name = "client_products", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "products")
    private List<String> products = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Claims> claims = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Orders> orders = new ArrayList<>();
}
