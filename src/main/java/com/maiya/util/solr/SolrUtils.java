package com.maiya.util.solr;

import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by lubin 2016/11/1
 */
public class SolrUtils {

    public static SingleColumnValueFilter scvf(String cf, String c, CompareFilter.CompareOp operator, String value) {
        return new SingleColumnValueFilter(
                Bytes.toBytes(cf),
                Bytes.toBytes(c),
                operator,
                Bytes.toBytes(value)
        );
    }

}
