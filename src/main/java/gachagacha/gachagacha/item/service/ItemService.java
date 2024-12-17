package gachagacha.gachagacha.item.service;

import gachagacha.gachagacha.item.dto.ReadItemsResponse;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.dto.AddItemResponse;
import gachagacha.gachagacha.item.entity.ItemType;
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
        String nickname = jwtUtils.getNicknameFromHeader(request);
        Item item = Item.findById(itemId);
        UserItem userItem = UserItem.create(item);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        user.addItem(userItem);
        userItemRepository.save(userItem);

        return new AddItemResponse(item.getItemId(), user.getNickname());
    }

    public ReadItemsResponse getItems(String nickname, HttpServletRequest request) {
        validateEditAuthorization(nickname, request);
        List<UserItem> userItems = userItemRepository.findByUserNickname(nickname);
        List<String> characterUrls = userItems.stream()
                .filter(userItem -> userItem.getItem().getItemType() == ItemType.CHARACTER)
                .map(userItem -> "/image" + userItem.getItem().getFilePath())
                .toList();
        List<String> backgroundUrls = userItems.stream()
                .filter(userItem -> userItem.getItem().getItemType() == ItemType.BACKGROUND)
                .map(userItem -> "/image" + userItem.getItem().getFilePath())
                .toList();
        return new ReadItemsResponse(characterUrls, backgroundUrls);
    }

    private void validateEditAuthorization(String nickname, HttpServletRequest request) {
        if (!jwtUtils.getNicknameFromHeader(request).equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
