package com.example.smart_elderly_care.jwt;

import com.example.smart_elderly_care.domain.entity.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomMemberDetails implements UserDetails {

    private final Member member;

    public CustomMemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + member.getRole().name());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 로그인 시 식별자는 전화번호(phone)
    @Override
    public String getUsername() {
        return member.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return member;
    }
}
