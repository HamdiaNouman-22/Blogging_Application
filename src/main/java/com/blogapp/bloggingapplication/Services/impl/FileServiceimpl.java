package com.blogapp.bloggingapplication.Services.impl;

import com.blogapp.bloggingapplication.Services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceimpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String name=file.getOriginalFilename();
        String randomID= UUID.randomUUID().toString();
        String filename1=
    randomID.concat(name.substring(name.lastIndexOf(".")));

    String filePath=path+ File.separator+filename1;
    File f=new File(path);
    if (!f.exists()){
        f.mkdir();
    }
        Files.copy(file.getInputStream(), Paths.get(filePath));
    return name;
    }

    @Override
    public InputStream getResource(String path, String filename) throws FileNotFoundException {
        String fullPath=path+File.separator+filename;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }
}
