package com.zjl.finalarchitecture.test



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

data class TEST(val pid: String, val entityId: String)

fun main(){
    addition_isCorrect()
}

fun addition_isCorrect() {
    val companyId = "1"

    val dataList = buildList {
        add(TEST("1", "2"))
        add(TEST("2", "3"))
        add(TEST("3", "4"))
        add(TEST("", "6"))
    }

    val sortMap = mutableListOf<TEST>()

    find(companyId, dataList, sortMap)

    println(sortMap)

}

private fun find(targetId: String, findList: List<TEST>, result: MutableList<TEST>): TEST? {
    val findItem = findList.find {
        it.pid == targetId
    }
    return if (findItem == null) {
        null
    } else {
        result.add(findItem)
        find(findItem.entityId, findList, result)
    }

}
