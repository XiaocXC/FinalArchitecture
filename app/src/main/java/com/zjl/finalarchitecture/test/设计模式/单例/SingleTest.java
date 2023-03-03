package com.zjl.finalarchitecture.test.设计模式.单例;

/**
 * @author Xiaoc
 * @since 2023-02-19
 **/
public class SingleTest {

    private SingleTest(){

    }

    // 饿汉式，线程安全
    private static SingleTest instance2 = new SingleTest();

    public static SingleTest getInstance2() {
        return instance2;
    }

    // 懒汉式，线程不安全
    private static SingleTest instance = null;

    public static SingleTest getInstance() {
        if(instance == null){
            instance = new SingleTest();
        }
        return instance;
    }

    // 双重锁
    public volatile static SingleTest instance3 = null;

    public static SingleTest getInstance3() {
        if(instance3 == null){
            synchronized (SingleTest.class){
                if(instance3 == null){
                    instance3 = new SingleTest();
                }
            }
        }
        return instance3;
    }
}
