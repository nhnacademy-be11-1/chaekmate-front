package shop.chaekmate.front.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import shop.chaekmate.front.auth.principal.CustomPrincipal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SecurityContextHolderTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void SecurityContextHolder에_CustomPrincipal_저장_및_조회_성공() {
        Long memberId = 1L;
        String name = "테스트사용자";
        String role = "ADMIN";
        CustomPrincipal principal = new CustomPrincipal(memberId, name, role);
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role));
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);

        Authentication retrievedAuth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(retrievedAuth).isNotNull();
        assertThat(retrievedAuth.isAuthenticated()).isTrue();

        Object retrievedPrincipal = retrievedAuth.getPrincipal();
        assertThat(retrievedPrincipal).isInstanceOf(CustomPrincipal.class);

        CustomPrincipal retrievedCustomPrincipal = (CustomPrincipal) retrievedPrincipal;
        assertThat(retrievedCustomPrincipal.getMemberId()).isEqualTo(memberId);
        assertThat(retrievedCustomPrincipal.getName()).isEqualTo(name);
        assertThat(retrievedCustomPrincipal.getRole()).isEqualTo(role);
        assertThat(retrievedAuth.getAuthorities())
                .hasSize(1)
                .extracting("authority")
                .contains("ROLE_ADMIN");
    }

    @Test
    void AuthenticationPrincipal로_사용_가능한_형태인지_확인() {
        CustomPrincipal principal = new CustomPrincipal(1L, "테스트사용자", "USER");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(auth);

        Authentication retrievedAuth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal retrievedPrincipal = (CustomPrincipal) retrievedAuth.getPrincipal();

        assertThat(retrievedPrincipal).isNotNull();
        assertThat(retrievedPrincipal.getMemberId()).isEqualTo(1L);
        assertThat(retrievedPrincipal.getName()).isEqualTo("테스트사용자");
        assertThat(retrievedPrincipal.getRole()).isEqualTo("USER");
    }

    @Test
    void SecurityContextHolder가_비어있을_때_null_반환() {
        SecurityContextHolder.clearContext();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth).isNull();
    }
}
