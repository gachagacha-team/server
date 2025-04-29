package gachagacha.domain.decoration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecorationItem {

    private Long userItemId;
    private Long itemId;
    private int x;
    private int y;
}
