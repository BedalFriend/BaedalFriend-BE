package com.hanghae.baedalfriend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae.baedalfriend.Mypage.dto.response.MypageImgResponseDto;
import com.hanghae.baedalfriend.dto.PhotoDto;
import com.hanghae.baedalfriend.dto.responsedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Transactional
    public List<PhotoDto> uploadImage(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return Collections.emptyList();
        }
        List<PhotoDto> photoDtos = new ArrayList<>();

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "????????? ???????????? ??????????????????.");
        }

        log.info("imageUrl : " + fileName);
        PhotoDto photoDto = PhotoDto.builder()
                .key(fileName)
                .path(amazonS3.getUrl(bucket, fileName).toString())
                .build();
        photoDtos.add(photoDto);


        return photoDtos;
    }

    public String upload(MultipartFile multipartFile) throws IOException {

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "????????? ???????????? ??????????????????.");
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }
    private String createFileName(String fileName) { // ?????? ?????? ????????? ???, ???????????? ??????????????? ?????? random?????? ????????????.
        // ?????? ????????? ????????? ?????? 100?????? ????????? (????????? varchar(255) ????????? ????????? ????????????)
        if (fileName.length()>100){
            fileName = fileName.substring(0,100);
        }
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file ????????? ????????? ????????? ???????????? ?????? ???????????? ??????, ?????? ????????? ???????????? ???????????? ??? ?????? ?????? ?????? .??? ?????? ????????? ??????.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????? ????????? ??????(" + fileName + ") ?????????.");
        }
    }

    public void deleteImage(String imageUrl) {
        String fileName = imageUrl.substring(54);
        System.out.println("fileName : " + fileName);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println("????????????");

    }

    public ResponseDto<?> createImage(MultipartFile multipartFile) throws IOException {
        String fileUrl = UUID.randomUUID().toString().concat(getFileExtension(multipartFile.getOriginalFilename()));

        ObjectMetadata objMeta = new ObjectMetadata(); //ContentLength??? S3??? ?????????????????? ??????
        objMeta.setContentLength(multipartFile.getInputStream().available());

        //S3??? API???????????? putObject??? ????????? ?????? Stream??? ?????? S3??? ?????? ?????????
        amazonS3.putObject(bucket, fileUrl, multipartFile.getInputStream(), objMeta);

        MypageImgResponseDto mypageImgResponseDto = MypageImgResponseDto.builder()
                .profileURL(amazonS3.getUrl(bucket, fileUrl).toString())
                .build();

        System.out.println("fileName : " + fileUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileUrl));

        return ResponseDto.success(mypageImgResponseDto);
    }
}