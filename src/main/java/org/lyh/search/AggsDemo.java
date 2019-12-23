package org.lyh.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketselector.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lyh
 * @version 2019-12-19 21:35
 */
public class AggsDemo {

    private static final Integer SIZE = 100;

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();

        /**
         * group by，having
         */
        Map<String, String> bucketsPathsMap = new HashMap<>();
        bucketsPathsMap.put("ageAvg", "ageAvg");
        Script script = new Script("params.ageAvg >= " + 30);
        BucketSelectorPipelineAggregationBuilder bs =
                PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, script);
        AggregationBuilder aggregationBuilder = AggregationBuilders
                .terms("countryBucket")
                .field("country")
                .size(SIZE)
                .subAggregation(AggregationBuilders
                        .avg("ageAvg")
                        .field("age"))
                .subAggregation(bs);

        SearchRequest searchRequest = new SearchRequest().indices(Constant.INDEX)
                .types(Constant.TYPE)
                .indicesOptions(IndicesOptions.lenientExpandOpen())
                .source(new SearchSourceBuilder()
                        .aggregation(aggregationBuilder)
                        .size(0)
                        .timeout(new TimeValue(50, TimeUnit.MILLISECONDS)));

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        Terms countryBucket = response.getAggregations().get("countryBucket");
        for (Terms.Bucket bucket : countryBucket.getBuckets()) {
            Avg avg = bucket.getAggregations().get("ageAvg");
            System.out.println(bucket.getKey() + " " + avg.getValue());
        }

        System.exit(0);
    }
}
