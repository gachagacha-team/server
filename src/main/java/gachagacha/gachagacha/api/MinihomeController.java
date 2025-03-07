package gachagacha.gachagacha.api;

import gachagacha.gachagacha.api.dto.response.AttendanceResponse;
import gachagacha.gachagacha.api.dto.response.CoinResponse;
import gachagacha.gachagacha.api.dto.response.MinihomeResponse;
import gachagacha.gachagacha.api.dto.request.AddGuestbookRequest;
import gachagacha.gachagacha.api.dto.request.EditGuestbookRequest;
import gachagacha.gachagacha.api.dto.response.GuestbookResponse;
import gachagacha.gachagacha.domain.attendance.Attendance;
import gachagacha.gachagacha.domain.guestbook.Guestbook;
import gachagacha.gachagacha.domain.minihome.Minihome;
import gachagacha.gachagacha.domain.user.Coin;
import gachagacha.gachagacha.domain.user.User;
import gachagacha.gachagacha.jwt.JwtUtils;
import gachagacha.gachagacha.domain.guestbook.GuestbookService;
import gachagacha.gachagacha.domain.minihome.MinihomeService;
import gachagacha.gachagacha.api.response.ApiResponse;
import gachagacha.gachagacha.domain.follow.FollowService;
import gachagacha.gachagacha.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MinihomeController {

    private final MinihomeService minihomeService;
    private final FollowService followService;
    private final UserService userService;
    private final GuestbookService guestbookService;
    private final JwtUtils jwtUtils;

    @Value("${image.api.endpoints.profile}")
    private String profileImageApiEndpoint;

    @Operation(summary = "미니홈 조회")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @GetMapping("/minihomes/{nickname}")
    public ApiResponse<MinihomeResponse> readMinihome(@PathVariable String nickname, HttpServletRequest request) {
        User minihomeUser = userService.readUserByNickname(nickname);
        Minihome minihome = minihomeService.visitMinihome(minihomeUser);
        User currentUser = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        int followersCnt = followService.readFollowersCnt(minihomeUser);
        int followingsCnt = followService.readFollowingsCnt(minihomeUser);
        boolean isFollowing = followService.isFollowing(currentUser, minihomeUser);
        return ApiResponse.success(MinihomeResponse.of(currentUser, minihomeUser, minihome, followersCnt, followingsCnt, isFollowing, profileImageApiEndpoint));
    }

    @Operation(summary = "방명록 조회(페이지네이션)")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    @Parameter(name = "pageable", description = "최신순(sort=createdAt,desc)")
    @GetMapping("/guestbooks/{nickname}")
    public ApiResponse<Page<GuestbookResponse>> readGuestbooks(@PathVariable String nickname, Pageable pageable, HttpServletRequest request) {
        User minihomeUser = userService.readUserByNickname(nickname);
        Minihome minihome = minihomeService.readMinihome(minihomeUser);
        Page<Guestbook> guestbooks = guestbookService.readGuestbooksByMinihome(minihome, pageable);
        String currentUserNickname = jwtUtils.getUserNicknameFromHeader(request);
        return ApiResponse.success(guestbooks
                .map(guestbook -> {
                    User author = (guestbook.getUserId() == -1) ? null : userService.readUserById(guestbook.getUserId());
                    return GuestbookResponse.of(guestbook, author, currentUserNickname);
                }));
    }

    @Operation(summary = "방명록 등록")
    @PostMapping("/guestbooks/{nickname}")
    @Parameter(name = "nickname", description = "미니홈 유저 닉네임")
    public ApiResponse<GuestbookResponse> addGuestbook(@PathVariable String nickname, @RequestBody AddGuestbookRequest addGuestbookRequest, HttpServletRequest request) {
        User minihomeUser = userService.readUserByNickname(nickname);
        Minihome minihome = minihomeService.readMinihome(minihomeUser);
        User author = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        long id = guestbookService.addGuestbook(minihome, author, addGuestbookRequest.getContent());
        Guestbook guestbook = guestbookService.readGuestbook(id);
        return ApiResponse.success(GuestbookResponse.of(guestbook, author, author.getNickname()));
    }

    @Operation(summary = "방명록 수정")
    @Parameter(name = "guestbookId", description = "수정할 방명록 id")
    @PutMapping("/guestbooks/{guestbookId}")
    public ApiResponse<GuestbookResponse> editGuestBook(@PathVariable long guestbookId, @RequestBody EditGuestbookRequest editGuestbookRequest, HttpServletRequest request) {
        Guestbook guestbook = guestbookService.readGuestbook(guestbookId);
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        long id = guestbookService.editGuestbook(guestbook, user, editGuestbookRequest.getContent());
        Guestbook updatedGuestbook = guestbookService.readGuestbook(id);
        return ApiResponse.success(GuestbookResponse.of(updatedGuestbook, user, user.getNickname()));
    }

    @Operation(summary = "방명록 삭제")
    @Parameter(name = "guestbookId", description = "삭제할 방명록 id")
    @DeleteMapping("/guestbooks/{guestbookId}")
    public ApiResponse deleteGuestBook(@PathVariable long guestbookId, HttpServletRequest request) {
        User user = userService.readUserByNickname(jwtUtils.getUserNicknameFromHeader(request));
        guestbookService.deleteGuestbook(guestbookId, user);
        return ApiResponse.success();
    }

    @Operation(summary = "코인 조회")
    @GetMapping("/coin")
    public ApiResponse<CoinResponse> getCoin(HttpServletRequest request) {
        Coin coin = userService.getCoin(jwtUtils.getUserNicknameFromHeader(request));
        return ApiResponse.success(CoinResponse.of(coin));
    }

    @Operation(summary = "출석체크")
    @PostMapping("/attend")
    public ApiResponse<AttendanceResponse> attend(HttpServletRequest request) {
        String nickname = jwtUtils.getUserNicknameFromHeader(request);
        User user = userService.readUserByNickname(nickname);
        Attendance attendance = userService.attend(user);
        return ApiResponse.success(AttendanceResponse.of(attendance, user));
    }
}
