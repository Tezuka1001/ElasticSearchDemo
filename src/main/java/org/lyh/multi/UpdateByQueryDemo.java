package org.lyh.multi;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.Collections;

/**
 * @author lyh
 * @version 2019-12-16 21:14
 */
public class UpdateByQueryDemo {

    private static final String SCRIPT_LANGUAGE = "painless";

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = RestClientUtil.getClient();
        BulkByScrollResponse response = client.updateByQuery(new UpdateByQueryRequest(Constant.INDEX)
                .setQuery(QueryBuilders.termQuery("name", "德莱厄斯"))
                .setScript(new Script(ScriptType.INLINE, SCRIPT_LANGUAGE, "ctx._source.age -= params.count", Collections.singletonMap("count", 4))), RequestOptions.DEFAULT);
        System.out.println(response.getBulkFailures().size());
        System.exit(0);
    }
}
