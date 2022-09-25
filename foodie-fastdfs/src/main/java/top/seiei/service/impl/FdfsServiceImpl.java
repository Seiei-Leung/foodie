package top.seiei.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.seiei.service.FdfsService;
import javax.annotation.Resource;
import java.io.IOException;

@Service
public class FdfsServiceImpl implements FdfsService {

    // Fastdfs storage 客户端，用于上传文件
    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String uploadFace(MultipartFile pictureFile, String extName) {
        try {
            // 上传图片，并获得 StorePath 对象
            StorePath storePath = fastFileStorageClient.uploadFile(pictureFile.getInputStream(), pictureFile.getSize(), extName, null);
            // getGroup 方法获取 卷名
            // getFullPath 方法获取全路径名
            // getPath 方法获取路径名（全路径名移除卷名）
            return storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean deleteFace(String filePath) {
        return null;
    }
}
