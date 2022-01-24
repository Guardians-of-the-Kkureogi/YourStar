package com.ssafy.yourstar.domain.member.controller;

import com.ssafy.yourstar.domain.member.db.entity.Member;
import com.ssafy.yourstar.domain.member.request.MemberPasswordPostReq;
import com.ssafy.yourstar.domain.member.request.MemberRegisterPostReq;
import com.ssafy.yourstar.domain.member.response.MemberLoginPostRes;
import com.ssafy.yourstar.global.model.response.BaseResponseBody;
import com.ssafy.yourstar.global.util.JwtTokenUtil;
import com.ssafy.yourstar.domain.member.request.MemberLoginPostReq;
import com.ssafy.yourstar.domain.member.service.MemberService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "회원 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "<strong>Email과 Password</strong>입력을 통해 로그인 한다.")
    public ResponseEntity<MemberLoginPostRes> memberLogin(@RequestBody @ApiParam(value = "로그인 정보", required = true) MemberLoginPostReq memberLoginInfo) {
        log.info("memberLogin - Call");

        String memberEmail = memberLoginInfo.getMemberEmail();
        String memberPassword = memberLoginInfo.getMemberPassword();

        if(memberService.loginApproveMember(memberEmail)) {
            Member member = memberService.loginMemberByMemberEmail(memberEmail);

            if(passwordEncoder.matches(memberPassword, member.getMemberPassword())){

                memberService.loginIsLoginMember(memberEmail); // 로그인 됐음을 DB에 저장 --> 로그인 여부 토큰에 담아서 보내기

                return ResponseEntity.ok(MemberLoginPostRes.of(201, "Success", JwtTokenUtil.getMemberLoginToken(memberEmail, member.getCode(), member.getMemberNick(), member.getIsLogin())));
            }else {
                // 비밀번호가 일치하지 않을 때
                return ResponseEntity.status(401).body(MemberLoginPostRes.of(401, "Invalid Password", null));
            }
        }else {
            // 회원가입 이메일 인증을 하지 않았을 때(권한 없음)
            return ResponseEntity.status(403).body(MemberLoginPostRes.of(403, "No permission", null));
        }
    }

    @ApiOperation(value = "회원가입", notes = "<strong>Email, Password, 이름, 닉네임, 성별, 휴대전화, 생일</strong>입력을 통해 회원가입 한다.")
    @PostMapping("/register")
    public ResponseEntity<? extends BaseResponseBody> memberRegister (@RequestBody MemberRegisterPostReq memberRegister){
        log.info("memberRegister - Call");

        memberService.registerMember(memberRegister);
        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
    }

    @ApiOperation(value = "회원가입 승인", notes = "<strong>이메일과 회원 고유 랜덤 코드</strong>를 통해 회원가입 인증을 한다.")
    @GetMapping("/register/approve/{memberEmail}")
    public ResponseEntity<? extends BaseResponseBody> memberRegisterApprove (@PathVariable String memberEmail) {
        log.info("memberRegisterApprove - Call");

        memberService.registerApproveMember(memberEmail);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @ApiOperation(value = "비밀번호 초기화", notes = "<strong>Email, 이름</strong>입력을 통해 비밀번호 초기화를 한다.")
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> memberPasswordInit (@RequestBody MemberPasswordPostReq memberPasswordPostReq) {
        log.info("memberPasswordInit - Call");

        memberService.passwordInitMember(memberPasswordPostReq);
        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
    }

    @ApiOperation(value = "이메일 중복 체크", notes = "회원가입 시, <strong>Email</strong>중복 체크를 한다.")
    @GetMapping("/email-check/{memberEmail}")
    public ResponseEntity<? extends BaseResponseBody> memberEmailCheck(@PathVariable String memberEmail) {
        log.info("memberEmailCheck - Call");

        // emailCheckMember가 true이면 사용할 수 있는 이메일
        if(memberService.emailCheckMember(memberEmail)) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }else {
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Invalid Email"));
        }
    }

    @ApiOperation(value = "닉네임 중복 체크", notes = "회원가입 시,<strong>닉네임</strong>중복 체크를 한다.")
    @GetMapping("/nick-check/{memberNick}")
    public ResponseEntity<? extends BaseResponseBody> memberNickCheck(@PathVariable String memberNick) {
        log.info("memberNickCheck - Call");

        // nickCheckMember가 true이면 사용할 수 있는 이메일
        if(memberService.nickCheckMember(memberNick)) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }else {
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Invalid NickName"));
        }
    }
}