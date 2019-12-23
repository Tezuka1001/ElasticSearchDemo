package org.lyh.single;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;

/**
 * @author lyh
 * @version 2019-12-09 20:52
 */
public class ExistsDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        /**
         * fetchSourceContext: 不返回任何字段
         * storedFields: 也不存储任何字段
         * store filed 和 _source的区别
         */
        boolean exists = client.exists(new GetRequest(Constant.INDEX, Constant.TYPE, "2")
                .fetchSourceContext(new FetchSourceContext(false))
                .storedFields("_none_"), RequestOptions.DEFAULT);
        System.out.println(exists);
        System.exit(0);
    }
}
