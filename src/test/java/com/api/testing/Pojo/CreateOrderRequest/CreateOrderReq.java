package com.api.testing.Pojo.CreateOrderRequest;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderReq {
    private List<OrderDetails> orders; 
}
