package com.zjl.finalarchitecture.test

/**
 * @description:
 * @author: zhou
 * @date : 2022/7/11 12:12
 */
class kotlin回调 {

    /**
     * 类似于java的
     * interface Test1Callback{
     *      void test1Callback()
     * }
     */
    private var test1Callback: () -> Unit = {}

    /**
     * 类似于java的
     * interface Test2Callback{
     *      void test2Callback(int a)
     * }
     */
    private var test2: (Int) -> Unit = {}


    /**
     * 类似于java的
     * interface Test3Callback{
     *      int test2Callback(int a)
     * }
     */
    private var test3: (Int) -> Int = {
        3
    }
}