package com.cbkim.apiserver;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// 어플리케이션이 구동되면서 실행되는 Class
// 어플리케이션이 구동될때 초기화처리가 필요하면 해당 Class에서 처리를 하면 된다.
@Component
@RequiredArgsConstructor
public class InitializeApiserverApplication implements ApplicationRunner{

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO Auto-generated method stub
        
    }
}
