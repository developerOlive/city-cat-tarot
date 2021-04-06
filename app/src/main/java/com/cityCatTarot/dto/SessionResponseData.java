package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 응답 정보.
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    @Mapping("accessToken")
    private String accessToken;
}
