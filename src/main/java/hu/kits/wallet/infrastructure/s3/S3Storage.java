package hu.kits.wallet.infrastructure.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import hu.kits.wallet.domain.FileStorage;

public class S3Storage implements FileStorage {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String folder = "kits-wallet-files";
    
    public S3Storage(String regionName, String accessKey, String secretKey, String bucketName) {
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        this.bucketName = bucketName;
    }

    @Override
    public byte[] getFile(String fileId) {
        try (S3Object object = s3Client.getObject(bucketName, createKey(fileId))) {
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (IOException ex) {
            throw new RuntimeException("Could not load file: " + fileId, ex); 
        }
    }

    @Override
    public void saveFile(String fileId, byte[] bytes) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        s3Client.putObject(bucketName, createKey(fileId), new ByteArrayInputStream(bytes), metadata);
    }

    @Override
    public void delete(String fileId) {
        s3Client.deleteObject(bucketName, createKey(fileId));
    }
    
    private String createKey(String fileId) {
        return folder + "/" + fileId;
    }

}
