package org.lyh.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author lyh
 * @version 2019-12-17 21:22
 */
public class SearchDemo {

    private static final String COUNTRY = "country";

    private static final String AGE = "age";

    private static final String GENDER = "gender";

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();

        /**
         * 1. 准备queryBuilder，也就是我们的查询条件，ES支持非常丰富的条件以及must/filter，must_not，should逻辑运算
         */
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery(COUNTRY, "诺克萨斯"))
                .should(QueryBuilders.termQuery(GENDER, "女"))
                .minimumShouldMatch(1)
                .filter(QueryBuilders.rangeQuery(AGE)
                        .gte(20)
                        .lte(40))
                .mustNot(QueryBuilders.termQuery(AGE, 30));

        /**
         * 2. 准备SearchSourceBuilder对象
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .fetchSource(null, "position")
                .query(queryBuilder)
                .sort(AGE, SortOrder.DESC)
                .from(0)
                .size(10)
                .timeout(new TimeValue(50, TimeUnit.MILLISECONDS));

        /**
         * 3. 准备SearchRequest对象
         * 指定index，变长参数，可以指定多个
         * 指定索引option，可以允许索引不存在，索引关闭等，可以阅读option源码详细了解
         * 指定source，就是查询条件
         */
        SearchRequest searchRequest = new SearchRequest().indices(Constant.INDEX)
                .types(Constant.TYPE)
                .indicesOptions(IndicesOptions.lenientExpandOpen())
                .source(searchSourceBuilder);

        /**
         * 4. 发起请求
         */
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        /**
         * 5. 解析结果
         */
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        System.exit(0);
    }
}
