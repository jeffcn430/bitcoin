package com.sf.bitcoin.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class HttpUtils {
    public static String doGet(RestTemplate restTemplate, String url, Map<String, Object> params) {
        ResponseEntity<String> getDistrictRes = restTemplate.getForEntity(url, String.class);
        return getDistrictRes.getBody();
    }

    public String doPost(RestTemplate restTemplate, String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .build();//.replaceQueryParams(params);
        ResponseEntity<String> getDistrictRes = restTemplate.exchange(builder.encode().toUri(), HttpMethod.GET, entity, String.class);
        return getDistrictRes.getBody();
    }
}
