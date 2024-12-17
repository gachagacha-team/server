package gachagacha.gachagacha.item.service;

import gachagacha.gachagacha.item.dto.AddBackgroundResponse;
import gachagacha.gachagacha.item.entity.Background;
import gachagacha.gachagacha.item.entity.BackgroundType;
import gachagacha.gachagacha.item.entity.ItemType;
import gachagacha.gachagacha.auth.jwt.JwtUtils;
import gachagacha.gachagacha.exception.ErrorCode;
import gachagacha.gachagacha.exception.customException.BusinessException;
import gachagacha.gachagacha.item.dto.AddItemResponse;
import gachagacha.gachagacha.item.entity.Item;
import gachagacha.gachagacha.item.repository.BackgroundRepository;
import gachagacha.gachagacha.item.repository.ItemRepository;
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

    private final ItemRepository itemRepository;
    private final BackgroundRepository backgroundRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AddItemResponse addItem(int itemTypeId, HttpServletRequest request) {
        String nickname = jwtUtils.getNicknameFromHeader(request);
        ItemType itemType = ItemType.findById(itemTypeId);
        Item item = Item.create(itemType);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        user.addItem(item);
        itemRepository.save(item);

        return new AddItemResponse(item.getId(), itemTypeId, user.getNickname());
    }

    public List<String> getItems(String nickname, HttpServletRequest request) {
        validateEditAuthorization(nickname, request);
        List<Item> items = itemRepository.findByUserNickname(nickname);
        List<String> itemImages = items.stream()
                .map(item -> "/image" + item.getItemType().getFilePath())
                .toList();
        return itemImages;
    }

    public AddBackgroundResponse addBackground(int backgroundId, HttpServletRequest request) {
        String nickname = jwtUtils.getNicknameFromHeader(request);
        BackgroundType backgroundType = BackgroundType.findById(backgroundId);
        Background background = Background.create(backgroundType);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        user.addBackground(background);
        backgroundRepository.save(background);

        return new AddBackgroundResponse(background.getId(), backgroundType.getId(), user.getNickname());
    }

    public List<String> getBackgrounds(String nickname, HttpServletRequest request) {
        validateEditAuthorization(nickname, request);
        List<Background> backgrounds = backgroundRepository.findByUserNickname(nickname);
        return backgrounds.stream()
                .map(background -> "/image" + background.getBackgroundType().getFilePath())
                .toList();
    }

    private void validateEditAuthorization(String nickname, HttpServletRequest request) {
        if (!jwtUtils.getNicknameFromHeader(request).equals(nickname)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
