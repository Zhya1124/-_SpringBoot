package com.wldemo.demo.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.wldemo.demo.exception.CustomizeErrorCode;
import com.wldemo.demo.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;
    @Value("${ucloud.ufile.region}")
    private String region;
    @Value("${ucloud.ufile.suffix}")
    private String suffix;
    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    //inputstream类型的文件流，文件类型，名字
    public String upload(InputStream fileStream,String mimetype, String fileName){
        //文件重命名阶段
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if(filePaths.length > 1){
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length-1];//用uuid重新生成一个唯一名字
        }else{
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
        }
        try {
            // Bucket相关API的授权器
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, suffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimetype)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {})
                    .execute();
                    if(response!=null && response.getRetCode() == 0){
                        String url = UfileClient.object(objectAuthorization, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName,bucketName,expires)
                                .createUrl();
                        return url;
                    }else{
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
                    }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
