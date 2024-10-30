package com.skku.skkuduler.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GcpConfig {

    @Value("${google.cloud.credentials}")
    private String credentials;

    @Bean
    public Storage storage() throws IOException {
        System.out.println(credentials);
        GoogleCredentials credential = GoogleCredentials.fromStream(new ByteArrayInputStream(credentials.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        return StorageOptions.newBuilder().setCredentials(credential).build().getService();
    }
}
