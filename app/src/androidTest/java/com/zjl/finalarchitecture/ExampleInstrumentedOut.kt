package com.zjl.finalarchitecture

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kongzue.baseokhttp.util.JsonMap
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedOut {

    private val testStr = "{\n" +
            "    \"success\": \"true\",\n" +
            "    \"Code\": \"\",\n" +
            "    \"Message\": \"\",\n" +
            "    \"ReturnData\": {\n" +
            "        \"RecordNumber\": 0,\n" +
            "        \"List\": []\n" +
            "    }\n" +
            "}"

    @Test
    fun useAppContext() {
        // Context of the app under test.

        val map = JsonMap.parse(testStr)
        val returnData = map.getJsonMap("ReturnData")
        Log.i("Test", returnData.toString())

        val obj = JSONObject(testStr)
        val returnDataObj = obj.optJSONObject("ReturnData")
        Log.i("Test", returnDataObj.toString())
    }
}