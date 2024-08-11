package com.FB_APP.demo.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue
    private Integer id ;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    @ToString.Exclude
    private Client client;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnoreProperties("orders")
    @ToString.Exclude
    private List<OrderProducts> products = new ArrayList<>();

    private String location;
    private Date date;
    private String time;

}
