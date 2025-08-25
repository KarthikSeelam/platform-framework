package com.ican.cortex.platform.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UserDTO extends BaseDTO{
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
