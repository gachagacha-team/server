package gachagacha.gachaapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public class ErrorResponse {

    private final long code;
    private final HttpStatus status;
    private final String message;
}
