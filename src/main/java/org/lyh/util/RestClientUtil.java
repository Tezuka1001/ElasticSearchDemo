package org.lyh.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author lyh
 * @version 2019-12-03 21:30
 */
public class RestClientUtil {

    private static final String HOST_NAME = "localhost";

    private static final int PORT = 9200;

    private static final String SCHEME = "http";

    private static final RestHighLevelClient CLIENT = new RestHighLevelClient(RestClient
            .builder(new HttpHost(HOST_NAME, PORT, SCHEME)));

    public static RestHighLevelClient getClient() {
        return CLIENT;
    }
}
