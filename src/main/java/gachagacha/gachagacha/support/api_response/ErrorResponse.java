package gachagacha.gachagacha.support.api_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final long code;
    private final HttpStatus status;
    private final String message;
}
