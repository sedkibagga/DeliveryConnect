package com.FB_APP.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comments {
    @Id
    @GeneratedValue
    private Integer id ;
    @Column(name = "comments", nullable = false)
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Client client;

}
