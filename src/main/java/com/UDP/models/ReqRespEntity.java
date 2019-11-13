package com.UDP.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReqRespEntity {
    private String hashSum;
    private String message;
    private int attempt;
}
