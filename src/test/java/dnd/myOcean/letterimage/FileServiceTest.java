package dnd.myOcean.letterimage;

import com.amazonaws.services.s3.AmazonS3Client;
import dnd.myOcean.letterimage.application.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(locations = "classpath:application-secret.yml")
class FileServiceTest {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String bucketUrl;

    @Mock
    private AmazonS3Client amazonS3Client;

    @InjectMocks
    private FileService fileService;

    private MultipartFile multipartFile;

    @BeforeEach
    void init_image() throws IOException {
        File initialFile = new File("src/test/resources/hello.png");

        // 1. hello.png에 대한 바이트 파일 생성
        InputStream createFile = new FileInputStream(initialFile);

        // 2. createFile 이름을 가진 MockFile 객체 생성
        multipartFile = new MockMultipartFile("file", "hello.png", MediaType.IMAGE_PNG_VALUE, createFile);

        // 3.fileService의 private 접근 지정자가 지정된 bucket, bucketUrl 필드 값 세팅 (Reflection 을 사용해)
        ReflectionTestUtils.setField(fileService, "bucket", bucket);
        ReflectionTestUtils.setField(fileService, "bucketUrl", bucketUrl);
    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    void upload_image() throws IOException {
        String uniqueFileName = "hello.png";
        String compareImageBucket = bucketUrl + "letter/" + uniqueFileName;

        String result = fileService.uploadImage(multipartFile, uniqueFileName);

        assertThat(compareImageBucket).isEqualTo(result);
    }
}