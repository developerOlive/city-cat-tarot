package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 채팅창 카드 응답 정보.
 */
public class TaroChatResponseData {

    @Mapping("cardId")
    private int cardId;

    @NotBlank
    @Mapping("cardImageUrl")
    private String cardImageUrl;

    @NotBlank
    @Mapping("cardTitle")
    private String cardTitle;

    @NotNull
    @Mapping("cardDetail")
    private String cardDetail;

}
