package gachagacha.gachaapi.common;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ApiResponse<S> {

    private final ResultType result;
    private final S data;
    private final ErrorResponse error;

    private ApiResponse(ResultType result, S data, ErrorResponse error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<?> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ErrorResponse error) {
        return new ApiResponse<>(ResultType.ERROR, null, error);
    }
}
