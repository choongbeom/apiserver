package com.cbkim.apiserver.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cbkim.apiserver.util.converter.ImageAttributeConverter;

@Builder                // builder를 사용할수 있게 합니다.
@Entity                 // jpa entity임을 알립니다.
@Getter
@Setter
@NoArgsConstructor      // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor     // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@EqualsAndHashCode(callSuper=false)      // equals, hashCode 자동 생성
@Table(name = "users",
    // 인텍싱 설정
    indexes = {
        @Index(columnList = "userid"),
        @Index(columnList = "user_code"),
        @Index(columnList = "name")
    }
)  // 'user' 테이블과 매핑됨을 명시
public class Users extends AuditorEntity implements UserDetails {

    private static final long serialVersionUID = 1L; //or @SuppressWarnings("serial") 하지만 UID 선언하는 것을 권장함.

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private long idx;

    @Column(name = "user_code", nullable = false, unique = true, columnDefinition = "char(10) comment 'user 코드'")
    private String userCode;

    @Column(name = "userid", nullable = false, unique = true, columnDefinition = "varchar(256) comment '아이디(이메일)'")
    private String userId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "userpw", nullable = false, columnDefinition = "varchar(100) comment '패스워드'")
    private String userPw;

    // 유저상태(UC0001) - USC001: 정상, USC002: 탈퇴, USC003: 휴면, USC004: 탈퇴요청, USC005: 계정정지
    // fetch = FetchType.LAZY: 사용 시점에 조회함.
    // optional = false: 해당 데이터가 필수적일때 사용(not null) = inner join으로 변경 (-> 사용 시 주의할 것!! 유니크 ID로 설정됨.)
    @OneToOne(targetEntity = CodeManagement.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_idx", nullable = false)
    private CodeManagement status;

    ////////////////////////////////////////////////////////////////////////////////////////
    // 기본정보
    ////////////////////////////////////////////////////////////////////////////////////////
    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) comment '이름'")
    private String name;

    @Column(name = "phone", columnDefinition = "varchar(50) comment '전화번호'")
    private String phone;   // 전화번호

    @Column(name = "email", nullable = false, columnDefinition = "varchar(1024) comment '이메일'")
    private String email;

    @Column(name = "nickname", columnDefinition = "varchar(100) comment '닉네임'")
    private String nickName;

    // 프로필 사진
    @Convert(converter = ImageAttributeConverter.class)
    @Column(name = "image", columnDefinition = "text comment '프로필 사진'")
    private Images image;

    @Column(name = "invitaion_code", columnDefinition = "char(10) comment '초대코드'")
    private String invitaionCode;

    @Column(name = "recommentdation_code", columnDefinition = "char(10) comment '추천인코드'")
    private String recommentdationCode;

    // 유저 프로필 문구
    @Column(name = "memo", columnDefinition = "varchar(2048) comment '프로필문구'")
    private String memo;

    // 주소
    @Builder.Default
    @Column(name = "address", columnDefinition = "varchar(1024) comment '주소'")
    private String address = "";

    @Builder.Default
    @Column(name = "address_sub", columnDefinition = "varchar(1024) comment '나머지주소'")
    private String addressSub = "";

    @Builder.Default
    @Column(name = "zipcode", columnDefinition = "char(10) comment '우편번호'")
    private String zipcode = "";

    // 생년월일
    @Column(name = "birth", columnDefinition = "char(8) comment '생년월일'")
    private String birth;

    // 성별
    @Column(name = "gender", columnDefinition = "char(6) comment '성별(male, female)'")  //
    private String gender;

    // 신장
    @Column(name = "height", columnDefinition = "int comment '신장'")  //
    private long height;

    // 몸무게
    @Column(name = "weight", columnDefinition = "int comment '몸무게'")  //
    private long weight;

    // 휴면계정 전환기간 (무기한/1년/2년) - UDC001: 1년, UDC002: 2년, UDC003: 무기한
    @OneToOne(targetEntity = CodeManagement.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "dormant_conversion_idx")
    private CodeManagement dormantConversion;

    // 이용약관 동의 여부
    @Builder.Default
    @Column(name = "terms_conditions_yn", columnDefinition = "char comment '이용약관 동의 여부'")
    private String termsConditionsYn = "Y";

    // 개인정보 수집 및 이용에 대한 동의 여부
    @Builder.Default
    @Column(name = "use_personal_information_yn", columnDefinition = "char comment '개인정보 수집 및 이용에 대한 동의 여부'")
    private String usePersonalInformationYn = "Y";

    // 마케팅 정보 수신 동의
    @Builder.Default
    @Column(name = "receive_marketing_email_yn", columnDefinition = "char comment '마케팅 정보 수신 동의 여부 - 이메일'")
    private String receiveMarketingEmailYn = "N";

    @Builder.Default
    @Column(name = "receive_marketing_sms_yn", columnDefinition = "char comment '마케팅 정보 수신 동의 여부 - 문자'")
    private String receiveMarketingSmsYn = "N";

    @Builder.Default
    @Column(name = "receive_marketing_kakaotalk_yn", columnDefinition = "char comment '마케팅 정보 수신 동의 여부 - 카카오톡'")
    private String receiveMarketingKakaotalkYn = "N";

    @Builder.Default
    @Column(name = "phone_auth", columnDefinition = "char comment '전화번호 인증여부'")
    private String phoneAuth = "N";  // 전화번호 인증

    // 가입경로 P01 1.0이관 P02 : 광고?
    @Column(name = "subscription_path", columnDefinition = "varchar(3) comment '가입경로'")
    private String subscriptionPath;

    @Column(name = "user_ci", columnDefinition = "varchar(100) comment '개인식별번호'")
    private String userCi;

    @Column(name = "user_di", columnDefinition = "varchar(100) comment '개인식별번호(PASS용)'")
    private String userDi;

    @Column(name = "user_fb", columnDefinition = "varchar(100) comment '파이어베이스 아이디'")
    private String userFb;

    @Column(name = "signin_device", columnDefinition = "varchar(1024) comment '로그인한 장비정보'")
    private String signinDevice;

    @Builder.Default
    @Column(name = "fcm_yn", columnDefinition = "varchar(1024) comment '모바일 알림여부'")
    private String fcmYn = "Y";

    @Column(name = "signin_session", columnDefinition = "varchar(1024) comment '자동로그인세션값'")
    private String signinSession;

    @Column(name = "last_signin", columnDefinition = "datetime comment '마지막접속일자'")
    private LocalDateTime lastSignin;    // 마지막접속일자

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Builder.Default
    @Column(name = "admin_yn", columnDefinition = "char comment '관리자 여부'")
    private String adminYn = "N";

    @Column(name = "delete_dt", columnDefinition = "datetime comment '탈퇴일자'")
    private LocalDateTime deleteDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.userPw;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.userId;
    }

    // 계정이 만료되지 않았는 지 리턴한다. (true: 만료안됨)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않았는 지 리턴한다. (true: 잠기지 않음)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는 지 리턴한다. (true: 만료안됨)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //  계정이 활성화(사용가능)인 지 리턴한다. (true: 활성화)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
