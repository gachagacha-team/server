package gachagacha.gachagacha.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

    A("A"),
    B("B"),
    C("C"),
    D("D")
    ;

    private final String viewName;
}
