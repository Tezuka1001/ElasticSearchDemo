package org.lyh.single;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.lyh.util.Constant;
import org.lyh.util.RestClientUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyh
 * @version 2019-12-03 21:53
 */
public class UpdateDemo {

    private static final RestHighLevelClient CLIENT = RestClientUtil.getClient();

    private static final String SCRIPT_LANGUAGE = "painless";

    public static void main(String[] args) throws IOException {

        upsert();
        System.exit(0);
    }

    /**
     * 脚本的参数顺序分别是：
     * 脚本类型：INLINE，STORED，FILE
     * 脚本语言：默认为painless
     * 脚本id或者源码，如果为INLINE则提供脚本源码，STORED则提供id，来寻找ES集群上存储的脚本，FILE指定文件，一般都是使用INLINE即可
     * 脚本参数
     */
    private static void updateByInlineScript() throws IOException {

        UpdateResponse updateResponse = CLIENT.update(new UpdateRequest(Constant.INDEX, Constant.TYPE, "1")
                .script(new Script(ScriptType.INLINE, SCRIPT_LANGUAGE, "ctx._source.age += params.count", Collections.singletonMap("count", 4))), RequestOptions.DEFAULT);
        System.out.println(updateResponse.getShardInfo().getFailed());
    }

    /**
     * doc的参数可以为Map，jsonString，jsonBuilder，Object变长数组
     */
    private static void updateByDoc() throws IOException {

        UpdateResponse updateResponse = CLIENT.update(new UpdateRequest(Constant.INDEX, Constant.TYPE, "1")
                .doc("name", "德莱厄斯", "age", 22), RequestOptions.DEFAULT);
        System.out.println(updateResponse.getShardInfo().getFailed());
    }

    /**
     * 如果更新的目标文档不存在，则插入upsert指定的doc
     */
    private static void upsert() throws IOException {
        Map<String, Object> doc = new HashMap<>(1<<2);
        doc.put("name", "德莱文");
        doc.put("country", "诺克萨斯");
        doc.put("birth", "2012-01-02");
        doc.put("age", 19);
        UpdateResponse updateResponse = CLIENT.update(new UpdateRequest(Constant.INDEX, Constant.TYPE, "3")
                .doc("name", "卡莎")
                .upsert(doc), RequestOptions.DEFAULT);
        System.out.println(updateResponse.getShardInfo().getFailed());
    }
}
