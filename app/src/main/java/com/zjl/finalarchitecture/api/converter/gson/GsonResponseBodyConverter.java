package com.zjl.finalarchitecture.api.converter.gson;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 玩安卓的GSON序列化器
 * 注意：该序列化器创建的Retrofit序列化处理器会自动脱壳[ResultVO]
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            JSONObject jsonObject = new JSONObject(response);
            // ----- 注意 -----
            // 可能我们有这种需求，例如一个提交接口，后台data什么也不返回，而我们需要使用ResultVO的Msg字段
            // 所以我们这里当业务成功时，而data为null时，我们序列化message的内容，这样可以更加灵活
            // 你只需要把你的泛型改成对应msg的类型即可
            JSONObject dataObj = jsonObject.optJSONObject("data");
            if(dataObj == null){
                // 如果data内容为null，我们尝试获取errorMsg作为内容
                JSONObject errorMsgObj = jsonObject.optJSONObject("errorMsg");
                if(errorMsgObj != null){
                    return transResponseBody(ResponseBody.create(errorMsgObj.toString(), value.contentType()));
                }
            }

            // 脱壳ResultVO
            // 这里由于拦截器里做了正误判断，我们直接脱壳就行，取data字段里的数据，进行序列化
            return transResponseBody(ResponseBody.create(dataObj.toString(), value.contentType()));
        } catch (JSONException e) {
            // 如果脱壳失败，可能是固定格式丢失，我们尝试用原始内容进行一次序列化
            return transResponseBody(value);
        }
    }

    private T transResponseBody(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}
