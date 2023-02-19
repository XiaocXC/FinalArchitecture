package com.zjl.finalarchitecture.test.设计模式.构建者模式;

import com.zjl.finalarchitecture.test.设计模式.构建者模式.TestConfig;

/**
 * @author Xiaoc
 * @since 2023-02-19
 **/
public class Test {

    public static void main(String[] args) {

        TestConfig config = new TestConfig.Builder()
                .setEnable1Builder(true).build();
    }
}
