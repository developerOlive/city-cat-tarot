package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 응답 정보.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResultData {
    @Mapping("id")
    private Long id;

    @Mapping("email")
    private String email;

    @Mapping("nickName")
    private String nickName;
}
