package com.maiya;


import com.maiya.beans.MemberContacts;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.ZkStateReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubin 2016/11/1
 * com.maiya.QueryData
 */
public class QueryData {

    /**
     * 根据索引字段查询并返回rowkey
     *
     * @param q      查询语句
     * @param server solr server
     * @return 返回 rowkey
     * @throws SolrServerException solr异常
     */
    private static ArrayList<String> queryIds(String q, SolrServer server) throws SolrServerException {

        ArrayList<String> ids = new ArrayList<String>();
        SolrQuery qry = new SolrQuery(q);
        QueryResponse response = server.query(qry);
        SolrDocumentList docs = response.getResults();

        //System.out.println("文档个数：" + docs.getNumFound()); //数据总条数也可轻易获取
        //System.out.println("查询时间：" + response.getQTime());

        for (SolrDocument doc : docs) {
            ids.add((String) doc.getFieldValue("id"));
        }
        return ids;
    }

    private static void add(SolrServer server) throws IOException, SolrServerException {
        MemberContacts contacts = new MemberContacts();
        contacts.setId("xca9fr1jbcscw7qqsg132i097ap96cyz,w4kxawsyeyixy41blzxt81fntdqv6qbe");
        contacts.setStelphone("13500000000");
        server.addBean(contacts);
    }

    private static ArrayList<String> get(HTable table, String cf, String col, FilterBase vf, ArrayList<String> ids) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        List<Get> list = new ArrayList<Get>();
        Get get;
        for (String id : ids) {
            get = new Get(Bytes.toBytes(id));
            if (vf != null) {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                filterList.addFilter(vf);
                get.setFilter(filterList);
            }
            list.add(get);
        }

        Result[] res = table.get(list);

        byte[] val;
        for (Result rs : res) {
            if (rs.containsNonEmptyColumn(cf.getBytes(), col.getBytes())) {
                val = rs.getValue(cf.getBytes(), col.getBytes());
                //对空值进行new String的话会抛出异常
                if (val != null && val.length > 0) {
                    result.add(new String(val));
                }
            }
        }
        return result;
    }

    /**
     * @param args 参数
     * @throws SolrServerException 异常
     * @throws IOException 异常
     */
    public static void main(String[] args) throws SolrServerException, IOException, SQLException {

        CloudSolrServer cloudSolrServer = new CloudSolrServer("hadoop04:2181,hadoop08:2181,hadoop06:2181,hadoop07:2181,hadoop05:2181/solr");
        cloudSolrServer.setDefaultCollection(args[0]);
        cloudSolrServer.setZkClientTimeout(20000);
        cloudSolrServer.setZkConnectTimeout(1000);
        cloudSolrServer.connect();

        ZkStateReader zkStateReader = cloudSolrServer.getZkStateReader();
        ClusterState cloudState = zkStateReader.getClusterState();
        System.out.println(cloudState);

        /*SolrQuery query = new SolrQuery();
        query.setQuery(args[1]);
        query.setRows(Integer.parseInt(args[2]));
        //query.setRows(Integer.MAX_VALUE);

        QueryResponse response = cloudSolrServer.query(query);
        SolrDocumentList docs = response.getResults();

        System.out.println("文档个数：" + docs.getNumFound());
        System.out.println("查询时间：" + response.getQTime());

        for (SolrDocument doc : docs) {
            String id = (String) doc.getFieldValue("id");
            System.out.println("id: " + id);
        }*/
        SolrInputDocument doc = new SolrInputDocument();


        cloudSolrServer.shutdown();
    }

}