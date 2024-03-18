package dnd.myOcean.letterimage.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.cloudfront.url}")
    private String bucketUrl;

    @Transactional
    public String uploadImage(final MultipartFile file, final String uniqueFileName) throws IOException {
        String folderName = "letter" + "/";
        String fileName = folderName + uniqueFileName;

        createResizeImage(bucket, fileName, file);

        return bucketUrl + folderName + uniqueFileName;
    }

    private void createResizeImage(final String bucketName, final String fileName, final MultipartFile image)
            throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        byte[] buffer = resizeImage(image);
        metadata.setContentLength(buffer.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);

        amazonS3Client.putObject(bucketName, fileName, inputStream, metadata);
    }

    private byte[] resizeImage(final MultipartFile image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(image.getInputStream()).size(400, 400).toOutputStream(outputStream);
        return outputStream.toByteArray();
    }
}
