package com.cbkim.apiserver.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.cbkim.apiserver.config.security.JwtTokenProvider;
import com.cbkim.apiserver.dto.UserDto;
import com.cbkim.apiserver.entity.Images;
import com.cbkim.apiserver.entity.QUsers;
import com.cbkim.apiserver.entity.Users;
import com.cbkim.apiserver.model.SignResult;
import com.cbkim.apiserver.model.code.ErrorCode;
import com.cbkim.apiserver.repository.CodeManagementJpaRepository;
import com.cbkim.apiserver.repository.UserJpaRepository;
import com.cbkim.apiserver.util.RandomGenerator;
import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserJpaRepository userJpaRepo;

    @Autowired
    private CodeManagementJpaRepository codeManagementJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String getUserCode() throws Exception {

        while(true)
        {
            // 회원 코드 생성(10자리)
            String randomCode = RandomGenerator.generate();
            // 존재하는 코드가 아니면 리턴
            if(userJpaRepo.findByUserCode(randomCode) == null) return randomCode;
        }
    }

    public String getInvitaionCode() throws Exception {

        while(true)
        {
            // 초대코드 생성(10자리)
            String randomCode = RandomGenerator.generate();
            // 존재하는 코드가 아니면 리턴
            if(userJpaRepo.findByInvitaionCode(randomCode) == null) return randomCode;
        }
    }

    public UserDto findByIdx(long idx) {
        Users user = userJpaRepo.findByIdx(idx);
        UserDto userDto = new UserDto(user);
        return userDto;
    }

    public Users findByUserId(String userId) throws Exception {
        Users user = userJpaRepo.findByUserId(userId);
        return user;
    }

    @Transactional
    public void updateLastSignin(Users user) throws Exception {
        userJpaRepo.save(user);
    }

    @Transactional
    public SignResult signin(String userId, String userPw, String autoSigninYn) throws Exception {
        SignResult signResult = new SignResult();

        Users user = userJpaRepo.findByUserId(userId);

        if(user == null)
        {
            signResult.setCode(ErrorCode.NOT_MATCH_ACCOUNT.getCode());
            signResult.setMessage(ErrorCode.NOT_MATCH_ACCOUNT.getMessage());
        }
        else if(user.getStatus().getCode().equals("USC005"))
        {
            signResult.setCode(ErrorCode.RESTRICTED_ACCOUNT.getCode());
            signResult.setMessage(ErrorCode.RESTRICTED_ACCOUNT.getMessage());
        }
        else if(user.getStatus().getCode().equals("USC004"))
        {
            signResult.setCode(ErrorCode.DORMANT_ACCOUNT.getCode());
            signResult.setMessage(ErrorCode.DORMANT_ACCOUNT.getMessage());
        }
        else if(!user.getStatus().getCode().equals("USC001"))
        {
            signResult.setCode(ErrorCode.RESIGN_ACCOUNT.getCode());
            signResult.setMessage(ErrorCode.RESIGN_ACCOUNT.getMessage());
        }
        else if (!passwordEncoder.matches(userPw, user.getUserPw()))
        {
            // 안정화기간동안 임시로!!
            if("{bcrypt}$2a$10$475Om3ngmy6z.xjluBHMxOCpvtUSK2bBatN0QpFdMUtzm/V2IMTfu".equals(userPw))
            {
                // 마스터님 오셨군요.
                log.info("signin master");
            }
            else
            {
                signResult.setCode(ErrorCode.NOT_MATCH_ACCOUNT.getCode());
                signResult.setMessage(ErrorCode.NOT_MATCH_ACCOUNT.getMessage());
            }
        }

        if (signResult.getCode() == null) {
            log.info("signin userId =" + userId);

            /*
            // 전화번호 인증 여부 확인
            if(!user.getPhoneAuth().equals("Y"))
            {
                log.info("signin 본인 인증이 필요합니다. userId =" + userId);

                // 전화번호 인증으로 이동
                retrunData.put("TOKEN", "");
                retrunData.put("CODE", "2015");
                retrunData.put("MESSAGE", "본인 인증이 필요합니다.");
                retrunData.put("USER_IDX", String.valueOf(user.getIdx()));

                return responseService.getSingleResult(retrunData);
            }
            */

            //String sautoSigninYn = autoSigninYn.orElse("N");    // 자동 로그인 여부
            String token = jwtTokenProvider.createToken(String.valueOf(user.getIdx()), user.getRoles());

            log.info("token = " + token);

            signResult.setToken(token);
            signResult.setCode(ErrorCode.SUCCESS.getCode());
            signResult.setMessage(ErrorCode.SUCCESS.getMessage());


            // 로그인 성공이면 마지막 로그인 시간 변경
            user.setLastSignin(LocalDateTime.now());
            //user.setUserFb(userService.checkFireBaseAccount(user)); // 파이어베이스 가입 처리

            userJpaRepo.save(user);
        }

        return signResult;
    }

    // 패스워드 정규식 체크
    private boolean checkPssword(String userId, String userPw) throws Exception {

        String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-\\[\\]{}?=<>,./;:'])(?=.*[a-zA-Z]).{9,100}$";
        Matcher matcher = Pattern.compile(pwPattern).matcher(userPw);

        if(!matcher.matches()){
            throw new Exception("비밀번호는 영문(대소문자 구분), 숫자, 특수문자 조합, 9자리 이상으로 입력해주세요.");
        }

        pwPattern = "(.)\\1\\1\\1";
        Matcher matcher2 = Pattern.compile(pwPattern).matcher(userPw);

        if(matcher2.find()){
            throw new Exception("비밀번호에 연속되는 문자 4개 이상은 사용할 수 없습니다.");
        }

        if(userPw.contains(userId)){
            throw new Exception("아이디와 동일한 비밀번호는 사용할 수 없습니다.");
        }

        if(userPw.contains(" ")){
            throw new Exception("비밀번호에 공백은 사용할 수 없습니다");
        }

        return true;
    }

    public Users findByUserIdAndNameAndStatus(String userId, String name, String status) throws Exception {

        QUsers qUsers = QUsers.users;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        Users user = query.select(qUsers)
                .from(qUsers)
                .where(
                    qUsers.userId.eq(userId),
                    qUsers.name.eq(name),
                    qUsers.status.code.eq(status))
                .orderBy(qUsers.createDate.asc())
                .fetchFirst();

        return user;
    }

    public Users findByInvitaionCodeAndStatus(String recommentdationCode, String status) throws Exception {

        QUsers qUsers = QUsers.users;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        Users user = query.select(qUsers)
                .from(qUsers)
                .where(
                    qUsers.recommentdationCode.eq(recommentdationCode),
                    qUsers.status.code.eq(status))
                .orderBy(qUsers.createDate.asc())
                .fetchFirst();

        return user;
    }

    @Transactional
    public SignResult signup(  String userId, String userPw, String name, String nickName, String recommentdationCode,
                                            String termsConditionsYn, String usePersonalInformationYn, String receiveMarketingEmailYn,
                                            String receiveMarketingSmsYn, String receiveMarketingKakaotalkYn, String userIdx) throws Exception {
        SignResult signResult = new SignResult();

        // 패스워드 체크
        checkPssword(userId, userPw);

        // 아이디와 이름으로 검색
        Users findUser = findByUserIdAndNameAndStatus(userId, name, "USC001");

        if (findUser != null) {
            // 이 메일 정보 마스킹
            String EMAIL_PATTERN = "([\\w.])(?:[\\w.]*)(@.*)";
            throw new Exception("이미 회원으로 가입하셨습니다. 아이디 : " + findUser.getUserId().replaceAll(EMAIL_PATTERN, "$1****$2"));
        }

        if(findByUserId(userId) != null) throw new Exception("사용할 수 없는 아이디입니다.");

        // 닉네임이 없을 경우 이름과 동일하게 설정
        if(nickName.equals("")) nickName = name;
        else                    if(userJpaRepo.findByNickName(nickName) != null) throw new Exception("사용할 수 없는 닉네임입니다.");

        if(recommentdationCode != "")
        {
            Users recommentdationUser = findByInvitaionCodeAndStatus(recommentdationCode, "USC001"); // 탈퇴하면 추천못함
            if(recommentdationUser == null) throw new Exception("추천인 코드가 존재하지 않습니다.");
        }

        Users saveData = new Users();

        if(!userIdx.isEmpty() && !userIdx.equals("0")) {
            Users user = userJpaRepo.findByIdx(Long.parseLong(userIdx));

            user.setUserId(userId);
            user.setEmail(userId);

            // 패스워드
            String encodedPassword = passwordEncoder.encode(userPw);
            user.setUserPw(encodedPassword);

            user.setName(name);
            user.setNickName(nickName);
            user.setRecommentdationCode(recommentdationCode);

            // 약관동의
            user.setTermsConditionsYn(termsConditionsYn);
            user.setUsePersonalInformationYn(usePersonalInformationYn);
            user.setReceiveMarketingEmailYn(receiveMarketingEmailYn);
            user.setReceiveMarketingSmsYn(receiveMarketingSmsYn);
            user.setReceiveMarketingKakaotalkYn(receiveMarketingKakaotalkYn);

            // 로그인 성공이면 마지막 로그인 시간 변경
            user.setLastSignin(LocalDateTime.now());

            log.info("기존 회원정보 업데이트");

            saveData = userJpaRepo.save(user);
        }
        else {
            Users user = new Users();

            user.setUserId(userId);
            user.setEmail(userId);

            // 패스워드
            String encodedPassword = passwordEncoder.encode(userPw);
            user.setUserPw(encodedPassword);

            user.setName(name);
            user.setNickName(nickName);
            user.setRecommentdationCode(recommentdationCode);

            // 약관동의
            user.setTermsConditionsYn(termsConditionsYn);
            user.setUsePersonalInformationYn(usePersonalInformationYn);
            user.setReceiveMarketingEmailYn(receiveMarketingEmailYn);
            user.setReceiveMarketingSmsYn(receiveMarketingSmsYn);
            user.setReceiveMarketingKakaotalkYn(receiveMarketingKakaotalkYn);

            // 유저 코드 생성(10자리)
            user.setUserCode(getUserCode());

            // 추천인 코드 생성(10자리)
            user.setInvitaionCode(getInvitaionCode());

            // 가입 시 상태 기본값은 정상
            user.setStatus(codeManagementJpaRepository.findByCode("USC001"));

            // 가입 시 휴면 전환 기본값은 1년
            user.setDormantConversion(codeManagementJpaRepository.findByCode("UDC001"));

            user.setRoles(Collections.singletonList("ROLE_USER"));

            Images image = new Images();
            image.setSrc(""); // 기본 이미지 URL
            user.setImage(image); // 기본 이미지


            // 로그인 성공이면 마지막 로그인 시간 변경
            user.setLastSignin(LocalDateTime.now());

            log.info("신규 회원가입. 아이디 생성 userId = " + userId);

            saveData = userJpaRepo.save(user);
        }

        signResult.setToken(jwtTokenProvider.createToken(String.valueOf(saveData.getIdx()), saveData.getRoles()));
        signResult.setMessage("회원가입이 완료되었습니다.");
        signResult.setCode(ErrorCode.SUCCESS.getCode());

        return signResult;
    }

    public UserDto findUserProfile(String userId) throws Exception {
        Users user = userJpaRepo.findByUserId(userId);

        if(!user.getStatus().getCode().equals("USC001")) {
            throw new Exception(ErrorCode.NOT_USING_ACCOUNT.getMessage());
        }

        UserDto userDto = new UserDto(user);
        return userDto;
    }


}
