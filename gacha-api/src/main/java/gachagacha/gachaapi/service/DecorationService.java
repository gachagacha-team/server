package gachagacha.gachaapi.service;

import gachagacha.domain.decoration.Decoration;
import gachagacha.domain.decoration.DecorationRepository;
import gachagacha.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecorationService {

    private final DecorationRepository decorationRepository;

    public void save(Decoration decoration, User user) {
        decorationRepository.save(decoration, user.getId());
    }

    public Decoration read(User user) {
        return decorationRepository.read(user);
    }
}
