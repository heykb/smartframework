package com.zhu.webtest.Service;

import com.zhu.annotation.Service;
import com.zhu.transactional.Transaction;

@Service
public class TestService {

    @Transaction
    public void test(){
        System.out.println("wo shi testService");
    }
}
