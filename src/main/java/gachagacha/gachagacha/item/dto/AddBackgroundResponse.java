package gachagacha.gachagacha.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddBackgroundResponse {

    private long backgroundId;
    private int backgroundTypeId;
    private String nickname;
}
