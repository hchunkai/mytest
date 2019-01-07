package index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class QueryManager {
    private void excQuery(Query query) throws Exception { ;
        System.out.println(query);
        Directory directory = FSDirectory.open(new File("E:\\java\\lucene\\data"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("bookName"));
            System.out.println("-------");
        }
    }

    @Test
    public void testTermQuery() throws Exception {
        TermQuery termQuery = new TermQuery(new Term("bookName", "放弃"));
        excQuery(termQuery);
    }

    @Test
    public void testNumericRangeQuery() throws Exception{
        Query getPrice = NumericRangeQuery.newDoubleRange("getPrice", 10d, 200d, false, false);
        excQuery(getPrice);
    }

    @Test
    public void testBooleanQuery() throws Exception{
        TermQuery termQuery = new TermQuery(new Term("bookName", "入门"));
        Query getPrice = NumericRangeQuery.newDoubleRange("getPrice", 10d, 200d, false, false);
        BooleanQuery booleanClauses = new BooleanQuery();
        booleanClauses.add(termQuery, BooleanClause.Occur.MUST_NOT);
        booleanClauses.add(getPrice, BooleanClause.Occur.MUST);
        excQuery(booleanClauses);
    }

    @Test
    public void testQueryParse()throws Exception {
        Analyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser(null, analyzer);
        //识别的是字符串，不能识别成数字
        Query query = queryParser.parse("bookName:java AND getPrice:100.0");
        excQuery(query);
    }

}

