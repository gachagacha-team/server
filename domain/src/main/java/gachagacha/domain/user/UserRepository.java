package gachagacha.domain.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {

    Slice<User> findAllBy(Pageable pageable);

    Optional<User> findBySocialTypeAndLoginId(SocialType socialType, Long loginId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findById(long userId);

    Long save(User user);

    void delete(User user);

    void update(User user);

    boolean existsBySocialTypeAndLoginId(SocialType socialType, long loginId);

    boolean existsByNickname(String nickname);
}
