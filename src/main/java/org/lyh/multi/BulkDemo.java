package org.lyh.multi;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyh
 * @version 2019-12-12 21:16
 */
public class BulkDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        Map<String, Object> doc = new HashMap<>(1 << 3);
        doc.put("title", "诺克萨斯之手");
        doc.put("name", "德莱厄斯");
        doc.put("country", "诺克萨斯");
        doc.put("gender", "男");
        doc.put("position", "战士");
        doc.put("age", 30);

        BulkResponse bulkResponse = client.bulk(new BulkRequest()
                .add(new UpdateRequest(Constant.INDEX, Constant.TYPE, "1").doc("age", 21))
                .add(new DeleteRequest(Constant.INDEX, Constant.TYPE, "2"))
                .add(new IndexRequest(Constant.INDEX, "2").source(doc))
                .timeout(TimeValue.timeValueMillis(50)), RequestOptions.DEFAULT);

        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();

            switch (bulkItemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    System.out.println(indexResponse.getShardInfo().getFailed());
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    System.out.println(updateResponse.getShardInfo().getFailed());
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    System.out.println(deleteResponse.getShardInfo().getFailed());
                default:
                    break;
            }
        }
        System.exit(0);
    }
}
