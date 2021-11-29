package com.cbkim.apiserver.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cbkim.apiserver.repository.UserJpaRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public UserDetails loadUserByUsername(String userPk) {
        return userJpaRepository.findById(Long.parseLong(userPk)).orElseThrow(() -> new UsernameNotFoundException(userPk));
    }
}
