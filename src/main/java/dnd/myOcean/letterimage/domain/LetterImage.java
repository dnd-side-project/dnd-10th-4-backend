package dnd.myOcean.letterimage.domain;


import dnd.myOcean.letterimage.exception.NoExtException;
import dnd.myOcean.letterimage.exception.UnSupportExtException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Arrays;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterImage {

    private static final String[] extension = {"jpg", "jpeg", "gif", "bmp", "png"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_image_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String uniqueName;

    @Column(nullable = false, updatable = false)
    private String originName;

    @Column(nullable = false, updatable = false)
    private String imagePath;

    public LetterImage(final String originName) {
        this.originName = originName;
        this.uniqueName = extractExtAndGenerateUniqueName(originName);
    }

    private String extractExtAndGenerateUniqueName(final String originName) {
        String ext = getExt(originName);
        return UUID.randomUUID() + "." + ext;
    }

    private String getExt(final String originName) {
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if (supportFormat(ext)) {
                return ext;
            } else {
                throw new UnSupportExtException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new NoExtException();
        } catch (UnSupportExtException e) {
            throw e;
        }
    }

    private boolean supportFormat(final String ext) {
        return Arrays.stream(extension)
                .anyMatch(e -> e.equalsIgnoreCase(ext));
    }

    public void updateImagePath(final String imagePath) {
        this.imagePath = imagePath;
    }
}
