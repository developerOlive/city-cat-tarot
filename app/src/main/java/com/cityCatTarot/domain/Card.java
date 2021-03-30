package com.cityCatTarot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 카드 정보.
 */

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card {

    @Id
//    @Column(name = "cardId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;

    @Column(name = "cardCategory")
    private String cardCategory;

    @Column(name = "cardImageUrl")
    private String cardImageUrl;

    @Column(name = "cardTitle")
    private String cardTitle;

    @Column(name = "cardDetail")
    private String cardDetail;

}
