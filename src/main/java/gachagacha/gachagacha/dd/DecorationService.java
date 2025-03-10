package gachagacha.gachagacha.dd;

import gachagacha.gachagacha.dd.dto.Decoration;
import gachagacha.gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecorationService {

    private final DecorationProcessor decorationProcessor;

    public void save(Decoration decoration, User user) {
        decorationProcessor.save(decoration, user.getId());
    }

    public Decoration read(User user) {
        return decorationProcessor.read(user);
    }
}
