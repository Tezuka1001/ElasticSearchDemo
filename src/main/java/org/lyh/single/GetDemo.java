package org.lyh.single;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.Map;

import static org.lyh.util.Constant.INDEX;

/**
 * @author lyh
 * @version 2019-12-03 21:34
 */
public class GetDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        Map<String, Object> map = client.get(new GetRequest(INDEX, Constant.TYPE, "2"), RequestOptions.DEFAULT).getSource();
        map.forEach((k, v) -> System.out.println(k + " " + v));
        System.exit(0);
    }
}
