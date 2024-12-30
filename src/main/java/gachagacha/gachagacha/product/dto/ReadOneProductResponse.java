package gachagacha.gachagacha.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadOneProductResponse {

    private String name;
    private String grade;
    private int quantity;
    private String imageUrl;
}
