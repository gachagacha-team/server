package gachagacha.gachagacha.item.service;

import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.ReadItemResponse;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.dto.AddItemResponse;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AddItemResponse addItem(int itemId, HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromHeader(request);
        Item item = Item.findById(itemId);
        UserItem userItem = UserItem.create(item);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        user.addItem(userItem);
        userItemRepository.save(userItem);

        return new AddItemResponse(item.getItemId(), user.getNickname());
    }

    public List<ReadBackgroundResponse> getBackgrounds(String nickname, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateEditAuthorization(user.getId(), request);

        return user.getBackgrounds().stream()
                .map(background -> new ReadBackgroundResponse(background.getId(), "/image" + background.getFilePath()))
                .toList();
    }

    public List<ReadItemResponse> getItems(String nickname, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateEditAuthorization(user.getId(), request);
        return userItemRepository.findByUserNickname(nickname).stream()
                .map(userItem -> new ReadItemResponse(userItem.getItem().getItemId(), "/image" + userItem.getItem().getFilePath()))
                .toList();
    }

    private void validateEditAuthorization(long userId, HttpServletRequest request) {
        if (jwtUtils.getUserIdFromHeader(request) != userId) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
