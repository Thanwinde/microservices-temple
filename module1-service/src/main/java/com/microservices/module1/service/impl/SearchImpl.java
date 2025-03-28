package com.microservices.module1.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.microservices.common.constant.ResultStatue;
import com.microservices.common.pojo.dto.Result;
import com.microservices.module1.config.RestClientConfig;
import com.microservices.module1.service.Search;
import io.github.classgraph.json.JSONUtils;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author nsh
 * @data 2025/3/27 19:59
 * @description
 **/
@Service
@RequiredArgsConstructor
public class SearchImpl implements Search {

    private final RestClientConfig restClientConfig;
    //获取elasticsearch的rest接口，用于发送请求

    @Override
    public Result search(String text) throws IOException {
        RestHighLevelClient restClient = restClientConfig.getClient();
        SearchRequest request = new SearchRequest("items");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //叶子搜索
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", text));//用must，必须，参与算分
        request.source().size(10);//结果规模
        request.source().query(boolQueryBuilder);
        SearchResponse searchResponse = restClient.search(request, RequestOptions.DEFAULT);
        JSONArray jsonArray = handleResponse(searchResponse);
        return new Result(ResultStatue.SUCCESS,"查询结果",jsonArray);
    }

    @Override
    public Result aggregations(String text,String field) throws IOException {
        RestHighLevelClient restClient = restClientConfig.getClient();
        SearchRequest request = new SearchRequest("items");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", text));
        request.source().query(boolQueryBuilder).size(0);
        request.source().aggregation(AggregationBuilders.terms(field + "_agg").field(field).size(10)
                .subAggregation(AggregationBuilders.max("max_price").field("price"))
                .subAggregation(AggregationBuilders.avg("avg_price").field("price"))
                .subAggregation(AggregationBuilders.min("min_price").field("price"))
                //添加Metric子聚合，列如最大值，平均值之类的
        );


        //聚合搜索
        SearchResponse response = restClient.search(request,RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms terms = aggregations.get(field + "_agg");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        JSONArray jsonArray = new JSONArray();
        //处理聚合结果
        for (Terms.Bucket bucket : buckets) {
            JSONObject te = new JSONObject();
            Max maxPrice = bucket.getAggregations().get("max_price");
            Min minPrice = bucket.getAggregations().get("min_price");
            Avg avgPrice = bucket.getAggregations().get("avg_price");
            te.put("name", bucket.getKeyAsString());
            te.put("value", bucket.getDocCount());
            te.put("max_price", maxPrice.getValue());
            te.put("min_price", minPrice.getValue());
            te.put("avg_price", avgPrice.getValue());
            jsonArray.add(te);
        }
        return new Result(ResultStatue.SUCCESS,"分类结果",jsonArray);

    }
    //处理结果，将其处理成json返回
    public JSONArray handleResponse(SearchResponse searchResponse){
        SearchHits searchHits = searchResponse.getHits();
        Long n = searchHits.getTotalHits().value;
        JSONArray jsonArray = new JSONArray();
        SearchHit[] searchHitArray = searchHits.getHits();
        for(SearchHit searchHit : searchHitArray){
            String source = searchHit.getSourceAsString();
            JSONObject te = JSONUtil.toBean(source, JSONObject.class);
            jsonArray.add(te);
        }
        return jsonArray;
    }
}
