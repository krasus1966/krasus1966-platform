package top.krasus1966.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.FileOutputStream;

/**
 * @author Krasus1966
 * @date 2020/8/18 21:10
 **/
public class FastDFSClient {

    private TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageServer storageServer = null;
    private StorageClient1 storageClient = null;


    public FastDFSClient(FastProperties conf) throws Exception {
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getTrackerServer();
        storageServer = null;
        storageClient = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * 上传文件方法
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     *
     * @param fileName 文件全路径
     * @param extName  文件扩展名，不包含（.）
     * @param metas    文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
        return storageClient.upload_file1(fileName, extName, metas);
    }

    public String uploadFile(String fileName) throws Exception {
        return uploadFile(fileName, null, null);
    }

    public String uploadFile(String fileName, String extName) throws Exception {
        return uploadFile(fileName, extName, null);
    }

    /**
     * 上传文件方法
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     *
     * @param fileContent 文件的内容，字节数组
     * @param extName     文件扩展名
     * @param metas       文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {

        return storageClient.upload_file1(fileContent, extName, metas);
    }

    public String uploadFile(byte[] fileContent) throws Exception {
        return uploadFile(fileContent, null, null);
    }

    public String uploadFile(byte[] fileContent, String extName) throws Exception {
        return uploadFile(fileContent, extName, null);
    }

    /**
     * 文件下载
     *
     * @param filePath 文件地址
     * @param savePath 本地保存地址
     */
    public void download(String filePath, String savePath) {
        try {
            byte[] bytes = storageClient.download_file1(filePath);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件删除
     *
     * @param filePath 文件的地址
     * @return
     */

    public Boolean deleteFile(String filePath) {
        try {
            int i = storageClient.delete_file1(filePath);
            return i == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件信息
     *
     * @param filePath 文件的地址
     * @return
     */
    public FileInfo getFileInfo(String filePath) {
        try {
//            FileInfo fileInfo = storageClient.get_file_info1(filePath);
//            String sourceIpAddr = fileInfo.getSourceIpAddr();//文件IP地址
//            long fileSize = fileInfo.getFileSize();//文件大小
//            Date createTimestamp = fileInfo.getCreateTimestamp();//文件创建时间
//            long crc32 = fileInfo.getCrc32();//错误码
            return storageClient.get_file_info1(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
