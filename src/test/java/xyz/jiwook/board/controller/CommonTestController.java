package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import xyz.jiwook.board.RestDocsConfiguration;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.entity.MemberEntity;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Import(RestDocsConfiguration.class)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public abstract class CommonTestController {
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepo memberRepo;

    protected static final String TEST_USERNAME = "testUsername";
    protected static final String TEST_PASSWORD = "testPassword";

    protected final String USABLE_ACCESS_TOKEN =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUiLCJpYXQiOjE3NDM0MzMyMDAsImV4cCI6NDEwMjY4ODQzOX0.PVplWk7y4UJtMqy_Z1_L8NUJQjZqYu07_1W1mAcz1ssV8sc8f3YZxPge5lDICtGxX_a3T88SOq79njfJpAnamw";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @BeforeEach
    public void DatabaseSetUp() {
        memberRepo.deleteAll();
        memberRepo.save(MemberEntity.builder()
                .username(TEST_USERNAME)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .nickname(TEST_USERNAME)
                .build());
    }
}
