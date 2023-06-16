package com.example.secure.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 토큰 정보를 응답하는데 사용할 dto
 */
public class TokenDto {

    private String token;
}
