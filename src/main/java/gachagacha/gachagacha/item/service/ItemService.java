package gachagacha.gachagacha.item.service;

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

import java.util.*;
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
        user.deductCoinForGacha();

        Item item = Item.gacha(ItemGrade.getItemGrade(new Random().nextInt(100) + 1));

        UserItem userItem = UserItem.create(item);
        user.addItem(userItem);

        return item.getImageFileName();
    }

    public List<UserItemResponse> readItemsByGrade(String nickname, String grade, HttpServletRequest request) {
        validateItemReadAuthorization(nickname, request);

        ItemGrade itemGrade = ItemGrade.findByViewName(grade);
        List<Item> items = Item.getItemsByGrade(itemGrade);

        Map<Long, List<Long>> itemIdToUserItemIds = userItemRepository.findByUserNickname(nickname).stream()
                .filter(userItem -> userItem.getItem().getItemGrade() == itemGrade)
                .collect(Collectors.groupingBy(
                        userItem -> userItem.getItem().getItemId(),
                        Collectors.mapping(
                                userItem -> userItem.getId(),
                                Collectors.toList()
                        )
                ));

        return items.stream()
                .map(item -> new UserItemResponse(item.getItemId(),
                        "/image/items/" + item.getImageFileName(),
                        item.getViewName(), item.getItemGrade().getViewName(),
                        itemIdToUserItemIds.get(item.getItemId()),
                        itemIdToUserItemIds.get(item.getItemId()).size(),
                        itemIdToUserItemIds.containsKey(item.getItemId())
                ))
                .toList();
    }

    public List<UserItemResponse> readAllItems(String nickname, HttpServletRequest request) {
        validateItemReadAuthorization(nickname, request);

        Map<Long, List<Long>> itemIdToUserItemIds = userItemRepository.findByUserNickname(nickname).stream()
                .collect(Collectors.groupingBy(
                        userItem -> userItem.getItem().getItemId(),
                        Collectors.mapping(
                                userItem -> userItem.getId(),
                                Collectors.toList()
                        )
                ));

        return Arrays.stream(Item.values())
                .map(item -> new UserItemResponse(item.getItemId(),
                        "/image/items/" + item.getImageFileName(),
                        item.getViewName(),
                        item.getItemGrade().getViewName(),
                        itemIdToUserItemIds.get(item.getItemId()),
                        itemIdToUserItemIds.get(item.getItemId()).size(),
                        itemIdToUserItemIds.containsKey(item.getItemId())
                ))
                .toList();
    }

    public List<ReadBackgroundResponse> readAllBackgrounds(String nickname, HttpServletRequest request) {
        User user = validateItemReadAuthorization(nickname, request);

        return user.getBackgrounds().stream()
                .map(background -> new ReadBackgroundResponse(background.getId(),
                        "/image/backgrounds/" + background.getImageFileName()
                ))
                .toList();
    }

    private User validateItemReadAuthorization(String nickname, HttpServletRequest request) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (jwtUtils.getUserIdFromHeader(request) != user.getId()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return user;
    }
}
