package com.cbkim.apiserver;

import static org.springframework.data.rest.webmvc.RestMediaTypes.HAL_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerTests {
  @Autowired
	MockMvc mockMvc;

	@Test
	public void get_user() throws Exception {

        mockMvc.perform(get("/v1/user")
            .accept(HAL_JSON)
            .param("idx", "1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-get",
                  resource(
                      ResourceSnippetParameters.builder()
                          .description("Get a user by idx")
                          .requestParameters(
                                  parameterWithName("idx").description("user idx"))
                          .responseFields(
                                  fieldWithPath("success").description("응답 성공여부 : true/false"),
                                  fieldWithPath("code").description("응답 코드 번호 : >= 0 정상, < 0 비정상"),
                                  fieldWithPath("msg").description("응답 메시지"),
                                  fieldWithPath("retoken").description("토큰 재발행"),
                                  fieldWithPath("data.idx").description("user idx"),
                                  fieldWithPath("data.userCode").description("user code"),
                                  fieldWithPath("data.userId").description("user id"),
                                  fieldWithPath("data.userPw").description("user password"),
                                  fieldWithPath("data.status.idx").description("user status idx"),
                                  fieldWithPath("data.status.code").description("user status code: USC001(정상) USC002(탈퇴) USC003(휴면) USC004(탈퇴요청) USC005(계정정지)"),
                                  fieldWithPath("data.status.codeValue").description("user status code value"),
                                  fieldWithPath("data.status.parentCode").description("user status parent code"),
                                  fieldWithPath("data.status.description").description("user status description"),
                                  fieldWithPath("data.name").description("user name"),
                                  fieldWithPath("data.phone").description("user phone"),
                                  fieldWithPath("data.email").description("user email"),
                                  fieldWithPath("data.nickName").description("user nickName"),
                                  fieldWithPath("data.image.alt").description("user image alt"),
                                  fieldWithPath("data.image.src").description("user image src"),
                                  fieldWithPath("data.image.titleYn").description("user image titleYn"),
                                  fieldWithPath("data.image.imageRef").description("user image Ref"),
                                  fieldWithPath("data.memo").description("user memo"),
                                  fieldWithPath("data.address").description("user address"),
                                  fieldWithPath("data.addressSub").description("user addressSub"),
                                  fieldWithPath("data.zipcode").description("user zipcode"),
                                  fieldWithPath("data.birth").description("user birth"),
                                  fieldWithPath("data.gender").description("user gender"),
                                  fieldWithPath("data.height").description("user height"),
                                  fieldWithPath("data.weight").description("user weight"))
                          .build()
                    )
      ));
	}

  @Test
	public void get_user_profile() throws Exception {

		mockMvc.perform(get("/v1/user/profile")
            .accept(HAL_JSON)
            .header("X-AUTH-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYzODk0OTkzMSwiZXhwIjoxNjM5MDM2MzMxfQ.VzxZPVbMTZvHifob6EFqI-K24UI5Xi1xn4ACIhyFx4U"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-profile-get",
                      resource(
                              ResourceSnippetParameters.builder()
                      .description("Get a user profile")

                      .responseFields(
                              fieldWithPath("success").description("응답 성공여부 : true/false"),
                              fieldWithPath("code").description("응답 코드 번호 : >= 0 정상, < 0 비정상"),
                              fieldWithPath("msg").description("응답 메시지"),
                              fieldWithPath("retoken").description("토큰 재발행"),
                              fieldWithPath("data.idx").description("user idx"),
                              fieldWithPath("data.userCode").description("user code"),
                              fieldWithPath("data.userId").description("user id"),
                              fieldWithPath("data.userPw").description("user password"),
                              fieldWithPath("data.status.idx").description("user status idx"),
                              fieldWithPath("data.status.code").description("user status code: USC001(정상) USC002(탈퇴) USC003(휴면) USC004(탈퇴요청) USC005(계정정지)"),
                              fieldWithPath("data.status.codeValue").description("user status code value"),
                              fieldWithPath("data.status.parentCode").description("user status parent code"),
                              fieldWithPath("data.status.description").description("user status description"),
                              fieldWithPath("data.name").description("user name"),
                              fieldWithPath("data.phone").description("user phone"),
                              fieldWithPath("data.email").description("user email"),
                              fieldWithPath("data.nickName").description("user nickName"),
                              fieldWithPath("data.image.alt").description("user image alt"),
                              fieldWithPath("data.image.src").description("user image src"),
                              fieldWithPath("data.image.titleYn").description("user image titleYn"),
                              fieldWithPath("data.image.imageRef").description("user image Ref"),
                              fieldWithPath("data.memo").description("user memo"),
                              fieldWithPath("data.address").description("user address"),
                              fieldWithPath("data.addressSub").description("user addressSub"),
                              fieldWithPath("data.zipcode").description("user zipcode"),
                              fieldWithPath("data.birth").description("user birth"),
                              fieldWithPath("data.gender").description("user gender"),
                              fieldWithPath("data.height").description("user height"),
                              fieldWithPath("data.weight").description("user weight"))
                      .build()
              )
      ));
	}

}
