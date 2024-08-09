package com.FB_APP.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "livraisons")
public class Livraison {
    @Id
    @GeneratedValue
    private Integer id ;
    private String localisation ;
    private String time ;
    private String num_tel;
    @Temporal(TemporalType.DATE)
    private Date date ;
    private String product_name ;
    private String status = "En cours" ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    @JsonBackReference
    @ToString.Exclude
    private User user;
}
