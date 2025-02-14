package gachagacha.gachagacha;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        throw new RuntimeException("에러 발생");
    }
}
