package top.krasus1966.fastdfs;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Krasus1966
 * @date 2020/8/18 21:46
 **/
@ConfigurationProperties(prefix = "krasus1966.fs.fastdfs")
public final class FastProperties {

    //second
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;
    //second
    public static final int DEFAULT_NETWORK_TIMEOUT = 30;
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final boolean DEFAULT_HTTP_ANTI_STEAL_TOKEN = false;
    public static final String DEFAULT_HTTP_SECRET_KEY = "FastDFS1234567890";
    public static final int DEFAULT_HTTP_TRACKER_HTTP_PORT = 80;
    public static final String[] DEFAULT_TRACKER_HTTP_SERVER = {"127.0.0.1:22122"};

    public static final boolean DEFAULT_CONNECTION_POOL_ENABLED = true;
    public static final int DEFAULT_CONNECTION_POOL_MAX_COUNT_PER_ENTRY = 100;
    //second
    public static final int DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME = 3600;
    //millisecond
    public static final int DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS = 1000;

    private Boolean enabled;
    private Integer connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private Integer networkTimeout = DEFAULT_NETWORK_TIMEOUT;
    private String fileCharset = DEFAULT_CHARSET;
    private Boolean antiStealToken = DEFAULT_HTTP_ANTI_STEAL_TOKEN;
    private String secretKey = DEFAULT_HTTP_SECRET_KEY;
    private Integer trackerServerPort = DEFAULT_HTTP_TRACKER_HTTP_PORT;
    private String[] trackerServer = DEFAULT_TRACKER_HTTP_SERVER;

    private Boolean useConnectionPool = DEFAULT_CONNECTION_POOL_ENABLED;
    private Integer connectionPoolMaxPreEntry = DEFAULT_CONNECTION_POOL_MAX_COUNT_PER_ENTRY;
    private Integer connectionPoolMaxIdleTime = DEFAULT_CONNECTION_POOL_MAX_IDLE_TIME;
    private Integer connectionPoolMaxWaitTimeInMs = DEFAULT_CONNECTION_POOL_MAX_WAIT_TIME_IN_MS;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(Integer networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

    public String[] getTrackerServer() {
        return trackerServer;
    }

    public void setTrackerServer(String[] trackerServer) {
        this.trackerServer = trackerServer;
    }

    public Boolean getUseConnectionPool() {
        return useConnectionPool;
    }

    public void setUseConnectionPool(Boolean useConnectionPool) {
        this.useConnectionPool = useConnectionPool;
    }

    public Integer getConnectionPoolMaxIdleTime() {
        return connectionPoolMaxIdleTime;
    }

    public void setConnectionPoolMaxIdleTime(Integer connectionPoolMaxIdleTime) {
        this.connectionPoolMaxIdleTime = connectionPoolMaxIdleTime;
    }

    public Integer getTrackerServerPort() {
        return trackerServerPort;
    }

    public void setTrackerServerPort(Integer trackerServerPort) {
        this.trackerServerPort = trackerServerPort;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean getAntiStealToken() {
        return antiStealToken;
    }

    public void setAntiStealToken(Boolean antiStealToken) {
        this.antiStealToken = antiStealToken;
    }

    public Integer getConnectionPoolMaxPreEntry() {
        return connectionPoolMaxPreEntry;
    }

    public void setConnectionPoolMaxPreEntry(Integer connectionPoolMaxPreEntry) {
        this.connectionPoolMaxPreEntry = connectionPoolMaxPreEntry;
    }

    public Integer getConnectionPoolMaxWaitTimeInMs() {
        return connectionPoolMaxWaitTimeInMs;
    }

    public void setConnectionPoolMaxWaitTimeInMs(Integer connectionPoolMaxWaitTimeInMs) {
        this.connectionPoolMaxWaitTimeInMs = connectionPoolMaxWaitTimeInMs;
    }

    public String getFileCharset() {
        return fileCharset;
    }

    public void setFileCharset(String fileCharset) {
        this.fileCharset = fileCharset;
    }
}
