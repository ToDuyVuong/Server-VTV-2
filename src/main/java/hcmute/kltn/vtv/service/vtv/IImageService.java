package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Async;

public interface IImageService {

    @Transactional
    String uploadImageToFirebase(MultipartFile multipartFile);

    @Async
    @Transactional
    void deleteImageInFirebase(String imageUrl);
}
