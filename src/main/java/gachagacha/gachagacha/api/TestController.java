package gachagacha.gachagacha.api;

import gachagacha.gachagacha.domain.item.ItemGrade;
import gachagacha.gachagacha.domain.lotto.LottoMessagePublisher;
import gachagacha.gachagacha.domain.outbox.LottoIssuanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final LottoMessagePublisher lottoMessagePublisher;

    @GetMapping("/test")
    public String test() {
        lottoMessagePublisher.publishLottoIssuanceEvent(new LottoIssuanceEvent(1, ItemGrade.S));
        return "success!";
    }
}
