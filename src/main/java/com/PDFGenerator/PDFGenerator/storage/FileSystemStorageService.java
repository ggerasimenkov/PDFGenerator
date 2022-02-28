package com.PDFGenerator.PDFGenerator.storage;

import com.PDFGenerator.PDFGenerator.utils.CONST;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Service
public class FileSystemStorageService implements StorageService {

    @Override
    public Resource loadAsResource() {
        try {
            Path file = Path.of(CONST.PATH_TO_FILE);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(CONST.FILE_NOT_READ);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException(CONST.FILE_NOT_READ, e);
        }
    }
}
