package dnd.myOcean.domain.letterimage.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String bucketUrl;

    @Transactional
    public String uploadImage(MultipartFile file) throws IOException {
        String folderName = "letter" + "/";
        String imageName = file.getOriginalFilename();
        String fileName = folderName + file.getOriginalFilename();

        createResizeImage(bucket, fileName, file);

        return bucketUrl + folderName + imageName;
    }

    private void createResizeImage(String bucketName, String fileName, MultipartFile image) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        byte[] buffer = resizeImage(image);
        metadata.setContentLength(buffer.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);

        amazonS3Client.putObject(bucketName, fileName, inputStream, metadata);
    }

    private byte[] resizeImage(MultipartFile image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(image.getInputStream()).size(400, 400).toOutputStream(outputStream);
        return outputStream.toByteArray();
    }
}
