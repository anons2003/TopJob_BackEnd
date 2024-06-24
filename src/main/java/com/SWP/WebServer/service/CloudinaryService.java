package com.SWP.WebServer.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public Map<String, Object> upload(MultipartFile file, String publicId,String folder) {
        try {
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", folder,
                    "overwrite", true
                    //set cho đẩy file lên cloudinary vào folder riêng biệt và tên gốc
            );
            Map<String, Object> data = this.cloudinary.uploader().upload(file.getBytes(), params);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
