package spring.reactive.web.java.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(RestDocumentationExtension.class)
class TestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = webTestClient
                .mutate()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void documentExampleApi() throws Exception {
        webTestClient.get().uri("/api/example")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentation.document("example"));
    }
}