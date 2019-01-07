package index;

import com.alibaba.lucene.po.Product;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import java.util.List;

public class TestSolr {

    /**
     * 说明：solr是根据pid域执行索引的更新。先根据pid域执行搜索，搜索到执行更新；搜索不到执行添加。
     * 	创建HttpSolrServer对象，连接solr服务
     * 	创建域相关的实体对象（Product）
     * 	使用HttpSolrServer对象，执行添加（更新）
     * 	提交事务
     */
    SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");

    /**
     * 添加或修改索引
     */
    @Test
    public void testSolr() throws Exception {
        Product product = new Product();
        product.setPid("8000");
        product.setName("iphone8");
        product.setCatalogName("手机");
        product.setPrice(8000);
        product.setDescription("苹果手机还不错哦！");
        product.setPicture("1.jpg");
        //  solrServer.addBean(product);
        solrServer.deleteByQuery("price:18.9");
        solrServer.commit();
    }

    @Test
    public void testQuerySolr() throws Exception {
        SolrQuery solrQuery = new SolrQuery("*.*");

    }

    }

