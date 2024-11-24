package com.karim.karim.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Component
public class UploadDirectoryInitializer {

    @Value("${file.path.upload-images}")
    private String uploadImagesPath;

    @Value("${file.path.upload-files}")
    private String uploadFilesPath;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        File imageDir = new File(uploadImagesPath);
        if (!imageDir.exists()) {
            boolean created = imageDir.mkdirs();
            if (!created) {
                throw new IllegalStateException("이미지 업로드 디렉토리를 생성할 수 없습니다.");
            }
        }
    }
}
