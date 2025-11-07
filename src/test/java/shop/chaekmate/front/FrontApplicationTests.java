package shop.chaekmate.front;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import shop.chaekmate.front.category.cache.CategoryCache;

@SpringBootTest
class FrontApplicationTests {

    @MockitoBean
    private CategoryCache categoryCache;

    @Test
	void contextLoads() {
        // 로드 테스트
	}

}
