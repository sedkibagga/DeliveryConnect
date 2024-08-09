package com.FB_APP.demo.orders.dtos;

import com.FB_APP.demo.entities.Claims;
import com.FB_APP.demo.entities.Comments;
import com.FB_APP.demo.entities.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderNameDto {
    private Integer id;
    private Integer userId;
    private String clientName;
    private List<Comments> comments = new ArrayList<>();
    private List<Claims> claims = new ArrayList<>();
    private List<Orders> orders = new ArrayList<>();
}
