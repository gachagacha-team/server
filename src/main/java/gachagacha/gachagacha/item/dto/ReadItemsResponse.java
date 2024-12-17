package gachagacha.gachagacha.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadItemsResponse {

    private List<String> characterUrls = new ArrayList<>();
    private List<String> backgroundUrls = new ArrayList<>();
}
