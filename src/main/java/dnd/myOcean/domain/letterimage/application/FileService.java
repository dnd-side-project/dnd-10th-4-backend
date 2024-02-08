package dnd.myOcean.domain.letterimage.application;

import dnd.myOcean.domain.letterimage.exception.FileUploadFailureException;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${upload.image.location}")
    private String location;

    @PostConstruct
    void postConstruct() { // 2
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void upload(MultipartFile file, String filename) {
        try {
            file.transferTo(new File(location + filename));
        } catch (IOException e) {
            throw new FileUploadFailureException();
        }
    }
}
