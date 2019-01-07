package index;

import com.alibaba.lucene.dao.BookDao;
import com.alibaba.lucene.dao.impl.BookDaoImpl;
import com.alibaba.lucene.po.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexManager {

    @Test
    public void creatIndex() throws Exception {
        //采集数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();
        //存储文档
        List<Document> documents = new ArrayList<>();
        for (Book book : bookList) {
            Document doc = new Document();
            doc.add(new TextField("bookName", book.getBookName(), Field.Store.YES));
            doc.add(new TextField("getBookDesc", book.getBookDesc(), Field.Store.YES));
            doc.add(new StoredField("getPic", book.getPic()));
            doc.add(new StringField("getId", book.getId() + "", Field.Store.YES));
            doc.add(new DoubleField("getPrice", book.getPrice(), Field.Store.YES));
            documents.add(doc);
        }
        //lucene自带的标准分词器
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建文件夹对象
        Directory directory = FSDirectory.open(new File("E:\\java\\lucene\\data"));
        //创建写索引对象
        IndexWriter indexWriter = new IndexWriter(directory, config);

        for (Document document : documents) {
            indexWriter.addDocument(document);
            indexWriter.commit();
        }
        indexWriter.close();
    }


    @Test
    public void testSearchIndex() throws Exception {

        Analyzer analyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        Query query = queryParser.parse("java");
        Directory directory = FSDirectory.open(new File("E:\\java\\lucene\\data"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs search = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id：" + doc.get("getId"));
            System.out.println("图书名称：" + doc.get("bookName"));
            System.out.println("图书价格：" + doc.get("getPrice"));
            System.out.println("图书图片：" + doc.get("getPic"));
            System.out.println("图书描述：" + doc.get("getBookDesc"));
        }
        indexReader.close();

    }

    @Test
    public void testTerm() throws Exception {
        Analyzer analyzer =new IKAnalyzer();
        Directory directory = FSDirectory.open(new File("E:\\java\\lucene\\data"));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        indexWriter.deleteDocuments(new Term("bookName","java"));
        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Analyzer analyzer =new IKAnalyzer();
        Directory directory = FSDirectory.open(new File("E:\\java\\lucene\\data"));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        Document document = new Document();
        document.add(new TextField("bookName","java入门到放弃", Field.Store.YES));
        Term term = new Term("BookName", "你好");
        indexWriter.updateDocument(term,document);
        indexWriter.commit();
        indexWriter.close();
    }


}



