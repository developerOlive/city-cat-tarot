package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModificationData {
//    @NotBlank
    @Mapping("nickName")
    private String nickName;

//    @NotBlank
//    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;
}
