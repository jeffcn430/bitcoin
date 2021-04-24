package com.sf.bitcoin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * http工具类
 *
 * @author admin
 */
@Slf4j
@Component
public class HttpUtils {
    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Object> defMap = new HashMap<>(1);

    /**
     * GET方式请求http
     *
     * @param url 请求地址
     * @return 返回json字符串
     */
    public String doGet(String url) {
        return this.doGet(url, null);
    }

    /**
     * GET方式请求http
     *
     * @param url   请求地址
     * @param key   参数名
     * @param value 参数值
     * @return 返回json字符串
     */
    public String doGet(String url, String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);
        return this.doGet(url, map);
    }

    /**
     * GET方式请求http
     *
     * @param url    请求地址
     * @param params 参数map
     * @return 返回json字符串
     */
    public String doGet(String url, Map<String, Object> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        Optional.ofNullable(params).orElse(this.defMap).forEach(builder::queryParam);

        ResponseEntity<String> getDistrictRes = restTemplate.getForEntity(builder.build().encode().toString(), String.class);
        if (getDistrictRes.getStatusCodeValue() != 200) {
            log.error("请求错误!url:{}", builder.build().encode().toString());
            return "";
        }
        return getDistrictRes.getBody();
    }

    public String doPost(RestTemplate restTemplate, String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
//                .replaceQueryParams(params)
                .build();
        ResponseEntity<String> getDistrictRes = restTemplate.exchange(builder.encode().toUri(), HttpMethod.GET, entity, String.class);
        return getDistrictRes.getBody();
    }
}
