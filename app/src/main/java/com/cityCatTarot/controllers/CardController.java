package com.cityCatTarot.controllers;

import com.cityCatTarot.application.CardService;
import com.cityCatTarot.domain.Card;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 카드에 대한 HTTP 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/tarotChat")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * 모든 카드를 응답합니다.
     */
    @GetMapping
    public List<Card> list() {
        return cardService.getCards();
    }

    /**
     * 전달된 식별자에 해당하는 카드정보를 응답합니다.
     *
     * @param
     * @return 전달된 식별자에 해당하는 카드
     */
    @GetMapping(path = "/{cardCategory}/{cardId}")
    public Card detail(@PathVariable (name = "cardCategory") String cardCategory,
                       @PathVariable (name = "cardId") int cardId) {

        return cardService.getCard(cardId);
    }
}
