package com.rest.api.controller.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rest.api.entity.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class UserControllerTest {
  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;
  private RestDocumentationResultHandler document;

  @Before
  public void setUp() {
    this.document = document("{class-name}/{method-name}", preprocessResponse(prettyPrint()));
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .apply(documentationConfiguration(this.restDocumentation).uris().withScheme("https").withHost("ezkorry.com")
            .withPort(443))
        .alwaysDo(document).build();
  }

  @Test
  public void post_db_test() throws Exception {

    User user = User.builder().uid("eszqsc112@naver.com").name("김태훈").build();

    String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(user);
    // Map<String, Object> data = new Gson().fromJson(jsonString, Map.class);

    mockMvc
        .perform(post("/v1/user")
            .contentType(MediaType.APPLICATION_JSON).content(jsonString).accept(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andDo(document.document(
            requestFields(
              fieldWithPath("name").description("이름"), 
              fieldWithPath("uid").description("이메일(아마도)"))));
  }

}