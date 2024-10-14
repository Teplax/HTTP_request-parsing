package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final String REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        try(CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();   ) {

            HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
            //request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            try(CloseableHttpResponse response = httpClient.execute(request)){

                List<CatFacts> catFacts = mapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<>(){}
                );
                catFacts.stream().filter(x->x.getUpvotes()!=null&&x.getUpvotes()>0).
                        forEach(System.out::println);
            }
        }


    }
}
