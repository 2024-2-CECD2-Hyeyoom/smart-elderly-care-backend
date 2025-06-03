package com.example.smart_elderly_care.service;

import com.example.smart_elderly_care.domain.entity.member.Member;
import com.example.smart_elderly_care.domain.repo.MemberRepository;
import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import com.example.smart_elderly_care.jwt.JwtToken;
import com.example.smart_elderly_care.jwt.JwtTokenProvider;
import com.example.smart_elderly_care.web.dto.member.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public LoginDTO.LoginResponseDTO login(LoginDTO.LoginRequestDTO request) {
        Member member = memberRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new CareClientException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CareClientException(ErrorStatus.INVALID_PASSWORD);
        }

        // 사용자 인증
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // JWT 토큰 생성
        JwtToken tokenInfo = jwtTokenProvider.generateToken(authentication);

        return LoginDTO.LoginResponseDTO.builder()
                .memberId(member.getId())
                .role(member.getRole().name())
                .token(tokenInfo.getAccessToken())
                .name(member.getName())
                .build();
    }
}
