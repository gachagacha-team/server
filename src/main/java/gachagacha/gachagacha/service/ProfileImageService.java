package gachagacha.gachagacha.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProfileImageService {

    @Value("${file.profile}")
    private String fileDir;

    public void storeProfileImage(MultipartFile file, String storeFileName) throws IOException {
        String directoryPath = System.getProperty("user.dir") + fileDir;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        file.transferTo(new File(directoryPath + storeFileName));
    }
}
