package com.chuyue.usercenter.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class MockUser {

    private Long id;
    private String username;
    private String avatar;
    private String role;

}
