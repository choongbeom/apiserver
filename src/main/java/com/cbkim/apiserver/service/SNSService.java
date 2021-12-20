package com.cbkim.apiserver.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.apis.KakaoApi;
import com.github.scribejava.apis.NaverApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SNSService {
	// google
	@Value("${spring.provider.google.client-id}")
	private String googleClientId;
	@Value("${spring.provider.google.client-secret}")
	private String googleClientSecret;

    // naver
    @Value("${spring.provider.naver.client-id}")
	private String naverClientId;
	@Value("${spring.provider.naver.client-secret}")
	private String naverClientSecret;

    // kakao
    @Value("${spring.provider.kakao.client-id}")
    private String kakaoClientId;

    // redirect
    private final String googleCallbackUrl = "http://localhost:8081/v1/signin/google";
    private final String naverCallbackUrl = "http://localhost:8081/v1/signin/naver";
    private final String kakaoCallbackUrl = "http://localhost:8081/v1/signin/kakao";

    // 참고사항: 임베디드 웹뷰(android, ios)에서 oauth2.0 구글 인증은 지원하지 않음.
    // 구글 계정으로 로그인 및 OAuth를 위해 구글에서 제공하는 SDK("Google Sign-In.")를 사용해서 구현해야 함
    // https://verypurple.tistory.com/35 (우회방법) - agent를 crome으로 변경하면 가능함
    public String getAuthorizationUrl(String provider) {

        String url = "";

        if ("google".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(googleClientId)
                                            .apiSecret(googleClientSecret)
                                            .defaultScope("email")
                                            .callback(googleCallbackUrl)
                                            .build(GoogleApi20.instance());

            url = service.getAuthorizationUrl();
        }
        else if ("naver".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(naverClientId)
                                            .apiSecret(naverClientSecret)
                                            .callback(naverCallbackUrl)
                                            .build(NaverApi.instance());

            url = service.getAuthorizationUrl();
        }
        else if ("kakao".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(kakaoClientId)
                                            .callback(kakaoCallbackUrl)
                                            .build(KakaoApi.instance());

            url = service.getAuthorizationUrl();
        }

        return url;
    }

    public OAuth2AccessToken getAccessToken(String provider, String scope, String code) throws IOException, InterruptedException, ExecutionException {

        OAuth2AccessToken accessToken = null;

        if ("google".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(googleClientId)
                                            .apiSecret(googleClientSecret)
                                            .defaultScope(scope)
                                            .callback(googleCallbackUrl)
                                            .build(GoogleApi20.instance());

            accessToken = service.getAccessToken(code);
        }
        else if ("naver".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(naverClientId)
                                            .apiSecret(naverClientSecret)
                                            .callback(naverCallbackUrl)
                                            .build(NaverApi.instance());

            accessToken = service.getAccessToken(code);
        }
        else if ("kakao".equals(provider)) {
            OAuth20Service service = new ServiceBuilder(kakaoClientId)
                                            .callback(kakaoCallbackUrl)
                                            .build(KakaoApi.instance());

            accessToken = service.getAccessToken(code);
        }

        return accessToken;
    }

    public String getGoogleUserProfile(String scope, OAuth2AccessToken accessToken) throws IOException, InterruptedException, ExecutionException {

        OAuth20Service service = new ServiceBuilder(googleClientId)
                                        .apiSecret(googleClientSecret)
                                        .defaultScope(scope)
                                        .callback(googleCallbackUrl)
                                        .build(GoogleApi20.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo");
        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            return response.getBody();
        }
	}

    public String getNaverUserProfile(OAuth2AccessToken accessToken) throws IOException, InterruptedException, ExecutionException {

        OAuth20Service service = new ServiceBuilder(naverClientId)
                                        .apiSecret(naverClientSecret)
                                        .callback(naverCallbackUrl)
                                        .build(NaverApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://openapi.naver.com/v1/nid/me");
        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            return response.getBody();
        }
	}

    public String getKakaoUserProfile(OAuth2AccessToken accessToken) throws IOException, InterruptedException, ExecutionException {

        OAuth20Service service = new ServiceBuilder(kakaoClientId)
                                        .callback(kakaoCallbackUrl)
                                        .build(KakaoApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://kapi.kakao.com/v2/user/me");
        service.signRequest(accessToken, request);

        try (Response response = service.execute(request)) {
            return response.getBody();
        }
	}
}
