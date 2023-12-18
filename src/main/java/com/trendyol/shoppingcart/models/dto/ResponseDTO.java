package com.trendyol.shoppingcart.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private boolean result;

    private T message;
}
