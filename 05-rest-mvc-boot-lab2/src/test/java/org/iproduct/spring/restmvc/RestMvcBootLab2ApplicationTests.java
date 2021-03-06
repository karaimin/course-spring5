package org.iproduct.spring.restmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.iproduct.spring.restmvc.config.SpringRootConfig;
import org.iproduct.spring.restmvc.config.SpringSecurityConfig;
import org.iproduct.spring.restmvc.config.SpringWebConfig;
import org.iproduct.spring.restmvc.dao.ArticleRepository;
import org.iproduct.spring.restmvc.model.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTags.uri;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestMvcBootLab2Application.class)
@AutoConfigureWebTestClient
@AutoConfigureJsonTesters
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
public class RestMvcBootLab2ApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;

	@Autowired
	private WebTestClient webClient;

	@Autowired
	private TestRestTemplate restTemplate;

    @MockBean
    private ArticleRepository articleRepository;

    @org.junit.Test
    @Test
    public void contextLoads() {
    }

    @LocalServerPort
	private int port;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void givenArticles_whenGetArticles_thenStatus200andJsonArray() throws Exception {

        given(articleRepository.findAll()).willReturn(mockArticles);

        webClient.get().uri("/api/articles").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(3).contains(mockArticles.get(0), mockArticles.get(1), mockArticles.get(2));

//                .expectBody().jsonPath("$").isArray().jsonPath("$.length()", new GreaterThan(1));
    }

    @Test
    public void givenArticles_whenGetArticles_thenStatus200andJsonArray_RestTemplate() throws Exception {

        given(articleRepository.findAll()).willReturn(mockArticles);

        List<Article> response = restTemplate.exchange("http://localhost:" + port + "/api/articles",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Article>>() {}).getBody();
        log.info("Response: {}", response);
        Assertions.assertEquals(mockArticles, response);
    }

    private static final List<Article> mockArticles = Arrays.asList(
            new Article("Welcome to Spring 5", "Spring 5 is great beacuse ..."),
            new Article("Dependency Injection", "Should I use DI or lookup ..."),
            new Article("Spring Beans and Wireing", "There are several ways to configure Spring beans.")
    );

    private static final Article newArticle =
            new Article("222222222222222222222222","New Title", "New content ...", LocalDateTime.now());

}
