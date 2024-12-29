package gachagacha.gachagacha.item.service;

import gachagacha.gachagacha.item.dto.ReadAllItemResponse;
import gachagacha.gachagacha.item.dto.ReadBackgroundResponse;
import gachagacha.gachagacha.item.dto.UserItemResponse;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.entity.ItemGrade;
import gachagacha.gachagacha.item.entity.UserItem;
import gachagacha.gachagacha.item.repository.UserItemRepository;
import gachagacha.gachagacha.user.entity.User;
import gachagacha.gachagacha.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public String gacha(HttpServletRequest request) {
        User user = userRepository.findById(jwtUtils.getUserIdFromHeader(request))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        user.gacha();

        ItemGrade itemGrade = ItemGrade.getItemGrade(new Random().nextInt(100) + 1);
        Item item = Item.gacha(itemGrade);

        UserItem userItem = UserItem.create(item);
        user.addItem(userItem);

        return item.getImageFileName();
    }

    public List<UserItemResponse> getItems(String nickname, String grade, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateEditAuthorization(user.getId(), request);
        ItemGrade itemGrade = ItemGrade.findByViewName(grade);
        List<Item> items = Item.getItems(itemGrade);

        Set<Long> itemIds = userItemRepository.findByUserNickname(nickname).stream()
                .filter(userItem -> userItem.getItem().getItemGrade() == itemGrade)
                .map(userItem -> userItem.getItem().getItemId())
                .collect(Collectors.toSet());

        return items.stream()
                .map(item -> new UserItemResponse(item.getItemId(), item.getViewName(), item.getItemGrade().getViewName(),
                        itemIds.contains(item.getItemId()), "/image/items/" + item.getImageFileName()))
                .toList();
    }

    public List<ReadAllItemResponse> getAllItems(String nickname, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateEditAuthorization(user.getId(), request);

        return userItemRepository.findByUserNickname(nickname).stream()
                .map(userItem -> new ReadAllItemResponse(userItem.getItem().getItemId(), "/image/items/" + userItem.getItem().getImageFileName()))
                .toList();
    }

    public List<ReadBackgroundResponse> getAllBackgrounds(String nickname, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        validateEditAuthorization(user.getId(), request);

        return user.getBackgrounds().stream()
                .map(background -> new ReadBackgroundResponse(background.getId(), "/image/backgrounds/" + background.getImageFileName()))
                .toList();
    }

    private void validateEditAuthorization(long userId, HttpServletRequest request) {
        if (jwtUtils.getUserIdFromHeader(request) != userId) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
