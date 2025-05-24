package com.example.smart_elderly_care.jwt;

import com.example.smart_elderly_care.domain.entity.member.Member;
import com.example.smart_elderly_care.domain.repo.MemberRepository;
import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhone(phone)
                .orElseThrow(() -> new CareClientException(ErrorStatus.PHONE_NOT_FOUND));

        return new CustomMemberDetails(member);
    }
}
