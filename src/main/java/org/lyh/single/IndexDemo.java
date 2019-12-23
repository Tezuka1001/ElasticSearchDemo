package org.lyh.single;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyh
 * @version 2019-12-03 21:52
 */
public class IndexDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        Map<String, Object> doc = new HashMap<>(1 << 3);
        doc.put("title", "德玛西亚之力");
        doc.put("name", "盖伦");
        doc.put("country", "德玛西亚");
        doc.put("gender", "男");
        doc.put("position", "战士");
        doc.put("age", 30);
        IndexResponse indexResponse = client.index(
                new IndexRequest(Constant.INDEX, Constant.TYPE, "5")
                        .source(doc), RequestOptions.DEFAULT);
        System.out.println(indexResponse.getShardInfo().getFailed());
        System.exit(0);
    }
}
