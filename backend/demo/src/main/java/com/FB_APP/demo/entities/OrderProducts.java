package com.FB_APP.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProducts {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer productId;
    private String productName;
    private String productPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id" , nullable = false)
    @JsonIgnoreProperties("products")
    private Orders orders;
}
