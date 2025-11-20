package shop.chaekmate.front.auth.interceptor;

import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FeignCookieInterceptorTest {

    @Mock
    private RequestTemplate requestTemplate;

    @Mock
    private HttpServletRequest mockRequest;

    private FeignCookieInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new FeignCookieInterceptor();
    }

    @Test
    void RequestAttributes가_null이면_헤더를_추가하지_않음() {
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(null);

            interceptor.apply(requestTemplate);

            verifyNoInteractions(requestTemplate);
        }
    }

    @Test
    void 쿠키가_없으면_헤더를_추가하지_않음() {
        when(mockRequest.getCookies()).thenReturn(null);

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verifyNoInteractions(requestTemplate);
        }
    }

    @Test
    void 쿠키_배열이_비어있으면_헤더를_추가하지_않음() {
        when(mockRequest.getCookies()).thenReturn(new Cookie[0]);

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verifyNoInteractions(requestTemplate);
        }
    }

    @Test
    void 단일_쿠키가_있으면_Cookie_헤더에_추가됨() {
        Cookie cookie = new Cookie("accessToken", "token123");
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verify(requestTemplate, times(1))
                    .header(eq("Cookie"), eq("accessToken=token123"));
        }
    }

    @Test
    void 여러_쿠키가_있으면_세미콜론으로_구분하여_Cookie_헤더에_추가됨() {
        Cookie accessToken = new Cookie("accessToken", "token123");
        Cookie refreshToken = new Cookie("refreshToken", "refresh456");
        Cookie sessionId = new Cookie("sessionId", "session789");

        when(mockRequest.getCookies()).thenReturn(new Cookie[]{
                accessToken, refreshToken, sessionId
        });

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verify(requestTemplate, times(1))
                    .header(eq("Cookie"), eq("accessToken=token123; refreshToken=refresh456; sessionId=session789"));
        }
    }

    @Test
    void 첫_번째_쿠키_앞에_세미콜론이_붙지_않음() {
        Cookie accessToken = new Cookie("accessToken", "token123");
        Cookie refreshToken = new Cookie("refreshToken", "refresh456");

        when(mockRequest.getCookies()).thenReturn(new Cookie[]{
                accessToken, refreshToken
        });

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verify(requestTemplate, times(1))
                    .header(eq("Cookie"), argThat(new ArgumentMatcher<String>() {
                        @Override
                        public boolean matches(String header) {
                            return header.startsWith("accessToken=") &&
                                    !header.startsWith("; ") &&
                                    header.contains("; refreshToken=");
                        }
                    }));
        }
    }

    @Test
    void 쿠키_값에_특수문자가_있어도_정상_처리됨() {
        Cookie cookie = new Cookie("accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMifQ");
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        try (MockedStatic<RequestContextHolder> mockedHolder = mockStatic(RequestContextHolder.class)) {
            mockedHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(attrs);

            interceptor.apply(requestTemplate);

            verify(requestTemplate, times(1))
                    .header(eq("Cookie"), eq("accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMifQ"));
        }
    }
}
