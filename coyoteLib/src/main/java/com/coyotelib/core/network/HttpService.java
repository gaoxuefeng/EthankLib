package com.coyotelib.core.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.coyotelib.core.util.JSON;
import com.coyotelib.core.util.coding.AbstractCoding;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class HttpService {

    public static final int DEFAULT_HTTP_TIMEOUT = 20 * 1000;
    private ArrayList<OnHttpResponseListener> mResponseJsonListeners = new ArrayList<OnHttpResponseListener>();
    public static final MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");
    // ISettingService settingService;

    public HttpService() {
        // this.settingService = settingService;
    }

    private void notifyResponseJsonListeners(URI request, String response) {
        for (OnHttpResponseListener listener : mResponseJsonListeners) {
            listener.onHttpResponse(request, response);
        }
    }

    public void register(OnHttpResponseListener listener) {
        try {
            mResponseJsonListeners.add(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] fetchBytesByGet(URI uri, int timeout) throws Exception {
        return fetchBytesByGet(uri, timeout, this.defaultCoding());
    }

    /**
     * Imp hint: choose a default coding
     */
    protected abstract AbstractCoding defaultCoding();

    /**
     * Call this function will notify registered OnHttpResponseListener
     */
    final public String fetchStringByPost(URI uri, RequestBody entity, int timeout, AbstractCoding coding) throws Exception {
        byte[] bytes = fetchBytesByPost(uri, entity, timeout, coding);
        String res = null;
        if (bytes != null) {
            try {
                res = new String(bytes, "utf-8");
                if (!TextUtils.isEmpty(res)) {
                    notifyResponseJsonListeners(uri, res);
                    res = String.valueOf(JSON.jsonFromString(res));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    final public String fetchStringByPost(URI uri, Map map, int timeout, AbstractCoding coding) throws Exception {
        byte[] bytes = fetchBytesByPost(uri, map, timeout, coding);
        String res = null;
        if (bytes != null) {
            try {
                res = new String(bytes, "utf-8");
                if (!TextUtils.isEmpty(res)) {
                    notifyResponseJsonListeners(uri, res);
                    res = String.valueOf(JSON.jsonFromString(res));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * Call this function will notify registered OnHttpResponseListener
     */
    final public String fetchStringByGet(URI uri, int timeout, AbstractCoding coding) throws Exception {
        byte[] bytes = fetchBytesByGet(uri, timeout, coding);
        String res = null;
        try {
            if (bytes == null) {
                return null;
            }
            res = new String(bytes, "utf-8");
            if (!TextUtils.isEmpty(res)) {
                notifyResponseJsonListeners(uri, res);
            }
            res = String.valueOf(JSON.jsonFromString(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Functions without coding will use defaultCoding() !!!
     */
    public final byte[] fetchBytesByPost(URI uri, Map<String, Object> kv) throws Exception {
        return fetchBytesByPost(uri, kv, DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public final byte[] fetchBytesByGet(URI uri) throws Exception {
        return fetchBytesByGet(uri, DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public final String fetchStringByGet(URI uri) throws Exception {
        return fetchStringByGet(uri, DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public final String fetchStringByPost(URI uri, Map<String, Object> kv) throws Exception {
        return fetchStringByPost(uri, mapToRequestBody(kv), DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public final JSONObject fetchJsonByGet(URI uri) throws Exception {
        return fetchJsonByGet(uri, DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public final JSONObject fetchJsonByGetWithoutCoding(URI uri) throws Exception {
        return fetchJsonByGet(uri, DEFAULT_HTTP_TIMEOUT, null);
    }

    public final JSONObject fetchJsonByPost(URI uri, Map<String, Object> kv) throws Exception {
        return fetchJsonByPost(uri, mapToRequestBody(kv), DEFAULT_HTTP_TIMEOUT, defaultCoding());
    }

    public byte[] fetchBytesByGet(URI uri, int timeout, AbstractCoding coding) throws Exception {
        if (timeout < 1000) {
            timeout = 1000;
        }
        Request request = new Request.Builder()
                .url(uri.toString())
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        builder.connectTimeout(1000, TimeUnit.MILLISECONDS);//单位毫秒
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return null;
        }
        if (response.isSuccessful()) {
            byte[] data = response.body().bytes();
            return null != coding ? coding.decode(data) : data;
        } else {
            return null;

        }
    }

    final public byte[] fetchBytesByGet(URI uri, AbstractCoding coding) throws Exception {
        return fetchBytesByGet(uri, DEFAULT_HTTP_TIMEOUT, coding);
    }

    private RequestBody mapToRequestBody(Map kv) throws Exception {
        /*FormBody.Builder builder = new FormBody.Builder();
        for (String key : kv.keySet()) {
            builder.add(key, kv.get(key));
        }*/

        RequestBody requestBody = RequestBody.create(JSONTYPE,JSONObject.toJSON(kv).toString());

        return requestBody;
    }

    final public byte[] fetchBytesByPost(URI uri, Map<String, Object> kv, int timeout, AbstractCoding coding) throws Exception {
        return fetchBytesByPost(uri, mapToRequestBody(kv), timeout, coding);
    }

    final public byte[] fetchBytesByPost(URI uri, RequestBody body, int timeout, AbstractCoding coding) throws Exception {

        Request request = new Request.Builder()

                .url(uri.toURL())

                .post(body)

                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        builder.connectTimeout(1000, TimeUnit.MILLISECONDS);//单位毫秒
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            byte[] bytes = response.body().bytes();
            return bytes != null ? coding.decode(bytes) : null;

        } else {
            return null;
        }
    }


    final public byte[] fetchBytesByPost(URI uri, RequestBody entity, AbstractCoding coding) throws Exception {
        return fetchBytesByPost(uri, entity, DEFAULT_HTTP_TIMEOUT, coding);
    }

    final public String fetchStringByPost(URI uri, Map<String, Object> kv, AbstractCoding coding) throws Exception {
        return fetchStringByPost(uri, mapToRequestBody(kv), DEFAULT_HTTP_TIMEOUT, coding);
    }

    final public String fetchStringByPost(URI uri, RequestBody entity, AbstractCoding coding) throws Exception {
        return fetchStringByPost(uri, entity, DEFAULT_HTTP_TIMEOUT, coding);
    }

    final public String fetchStringByGet(URI uri, AbstractCoding coding) throws Exception {
        return fetchStringByGet(uri, DEFAULT_HTTP_TIMEOUT, coding);
    }

    final public JSONObject fetchJsonByGet(URI uri, int timeout, AbstractCoding coding) throws Exception {
        String str = fetchStringByGet(uri, timeout, coding);
        return JSONObject.parseObject(str);
    }



    final public JSONObject fetchJsonByPost(URI uri, RequestBody entity, int timeout, AbstractCoding coding) throws Exception {
        String str = fetchStringByPost(uri, entity, timeout, coding);
        return JSONObject.parseObject(str);
    }

    final public JSONObject fetchJsonByPost(URI uri, Map<String, Object> kv, AbstractCoding coding) throws Exception {
        return fetchJsonByPost(uri, mapToRequestBody(kv), DEFAULT_HTTP_TIMEOUT, coding);
    }

    final public JSONObject fetchJsonByPost(URI uri, RequestBody entity, AbstractCoding coding) throws Exception {
        return fetchJsonByPost(uri, entity, DEFAULT_HTTP_TIMEOUT, coding);
    }


    public final JSONObject repeatfetchJsonByGet(URI uri, AbstractCoding coding, int retryCount, int intervalTimeoutMillis) {
        while (retryCount-- > 0) {
            JSONObject json = null;
            try {
                json = fetchJsonByGet(uri, intervalTimeoutMillis, coding);
            } catch (Exception e) {
                continue;
            }
            if (json != null) {
                return json;
            }
        }
        return null;
    }

    public final JSONObject repeatfetchJsonByGet(URI uri, int retryCount, int intervalTimeoutMillis) {
        return this.repeatfetchJsonByGet(uri, defaultCoding(), retryCount, intervalTimeoutMillis);
    }

    public final JSONObject repeatfetchJsonByGet(URI uri, int retryCount) {
        return this.repeatfetchJsonByGet(uri, retryCount, DEFAULT_HTTP_TIMEOUT);
    }

    private static final String KEY_AUTH_SESSION = "com.simiao.yaogeili.auth_session";


    public void setAuth(String val) {
        if (!TextUtils.isEmpty(val)) {
            // settingService.setString(KEY_AUTH_SESSION, val);

        }
    }


}
