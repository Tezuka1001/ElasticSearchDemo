package org.lyh.search;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;

/**
 * @author lyh
 * @version 2019-12-23 21:27
 */
public class CountDemo {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();

        /**
         * where 条件
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery());

        /**
         * 发起count请求
         */
        CountResponse response = client.count(new CountRequest().indices(Constant.INDEX)
                .indicesOptions(IndicesOptions.lenientExpandOpen())
                .source(searchSourceBuilder), RequestOptions.DEFAULT);

        System.out.println(response.getCount());
        System.exit(0);
    }
}
