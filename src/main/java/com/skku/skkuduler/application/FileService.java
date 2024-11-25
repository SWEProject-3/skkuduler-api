package com.skku.skkuduler.application;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

    public String storeFile(MultipartFile file){
        if(file == null || file.isEmpty() || file.getOriginalFilename() == null) return "";
        String fileExtension = "";
        String originalFileName = file.getOriginalFilename();
        if (originalFileName.contains(".")) {
            fileExtension = file.getOriginalFilename().substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + fileExtension;
        try {
            storage.create(
                    BlobInfo.newBuilder(bucketName, fileName).build(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new ErrorException(Error.FILE_STORE_ERROR);
        }
        return fileName;

    }

    public InputStream loadFile(String fileName) {
        Blob blob = storage.get(bucketName,fileName);
        ReadChannel reader = blob.reader();
        return Channels.newInputStream(reader);
    }
}
