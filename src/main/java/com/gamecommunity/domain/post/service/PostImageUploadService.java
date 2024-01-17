package com.gamecommunity.domain.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostImageUploadService {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private String defaultUrl = "https://airplanning-bucket.s3.ap-northeast-2.amazonaws.com";

  public String uploadFile(MultipartFile file) throws IOException {

    String bucketDir = bucketName;
    String dirUrl = defaultUrl + "/";
    String fileName = generateFileName(file);

    amazonS3Client.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));
    return dirUrl + fileName;

  }

  // multipartfile의 사이즈와 타입을 S3에 알려주기 위해서 ObjectMetadata를 사용
  private ObjectMetadata getObjectMetadata(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());
    return objectMetadata;
  }

  private String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
  }
}
