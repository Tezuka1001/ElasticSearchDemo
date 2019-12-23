package org.lyh.single;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;

/**
 * @author lyh
 * @version 2019-12-03 21:52
 */
public class DeleteDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        DeleteResponse deleteResponse = client.delete(new DeleteRequest(Constant.INDEX, Constant.TYPE, "1"), RequestOptions.DEFAULT);
        System.out.println(deleteResponse.getShardInfo().getFailed());
        System.exit(0);
    }
}
