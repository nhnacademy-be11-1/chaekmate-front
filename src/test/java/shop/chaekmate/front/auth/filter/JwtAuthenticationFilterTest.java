package shop.chaekmate.front.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import shop.chaekmate.front.auth.dto.response.MemberInfoResponse;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.auth.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtAuthenticationFilterTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 쿠키에_accessToken이_있고_Auth_Server에서_사용자_정보를_성공적으로_받아오면_SecurityContextHolder에_저장() throws Exception {
        String accessToken = "test-access-token";
        Cookie cookie = new Cookie("accessToken", accessToken);
        Cookie[] cookies = { cookie };

        MemberInfoResponse memberInfo = new MemberInfoResponse(1L, "테스트사용자", "ADMIN");
        ResponseEntity<MemberInfoResponse> responseEntity = ResponseEntity.ok(memberInfo);

        when(request.getCookies()).thenReturn(cookies);
        when(authService.getMemberInfo(accessToken)).thenReturn(responseEntity);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();

        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        assertThat(principal.getMemberId()).isEqualTo(1L);
        assertThat(principal.getName()).isEqualTo("테스트사용자");
        assertThat(principal.getRole()).isEqualTo("ADMIN");

        verify(authService).getMemberInfo(accessToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void 쿠키에_accessToken이_없으면_SecurityContextHolder에_저장하지_않음() throws Exception {
        when(request.getCookies()).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();

        verify(authService, never()).getMemberInfo(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void Auth_Server_호출_실패시_SecurityContextHolder_초기화() throws Exception {
        String accessToken = "invalid-token";
        Cookie cookie = new Cookie("accessToken", accessToken);
        Cookie[] cookies = { cookie };

        when(request.getCookies()).thenReturn(cookies);
        when(authService.getMemberInfo(accessToken)).thenThrow(new RuntimeException("Auth Server 오류"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();

        verify(authService).getMemberInfo(accessToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void Auth_Server_응답이_200번대가_아니면_SecurityContextHolder에_저장하지_않음() throws Exception {
        String accessToken = "test-token";
        Cookie cookie = new Cookie("accessToken", accessToken);
        Cookie[] cookies = { cookie };

        ResponseEntity<MemberInfoResponse> errorResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        when(request.getCookies()).thenReturn(cookies);
        when(authService.getMemberInfo(accessToken)).thenReturn(errorResponse);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();

        verify(authService).getMemberInfo(accessToken);
        verify(filterChain).doFilter(request, response);
    }
}
