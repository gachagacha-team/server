package gachagacha.gachaapi.controller;

import gachagacha.gachaapi.dto.response.ReadLottoResponse;
import gachagacha.gachaapi.common.ApiResponse;
import gachagacha.gachaapi.auth.jwt.JwtUtils;
import gachagacha.gachaapi.service.LottoService;
import gachagacha.domain.user.User;
import gachagacha.gachaapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LottoController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final LottoService lottoService;

    @GetMapping("/lottos")
    public ApiResponse<List<ReadLottoResponse>> readLottos(HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        List<ReadLottoResponse> data = lottoService.findUnusedLottos(user)
                .stream()
                .map(lotto -> ReadLottoResponse.of(lotto))
                .toList();
        return ApiResponse.success(data);
    }

    @PutMapping("/lotto/use/{lottoId}")
    public ApiResponse useLotto(@PathVariable long lottoId, HttpServletRequest request) {
        User user = userService.readUserById(jwtUtils.getUserIdFromHeader(request));
        lottoService.useLotto(lottoId, user);
        return ApiResponse.success();
    }
}
