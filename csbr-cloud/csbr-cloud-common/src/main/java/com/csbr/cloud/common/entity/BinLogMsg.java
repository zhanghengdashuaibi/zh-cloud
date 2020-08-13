package com.csbr.cloud.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhangheng
 * @date 2020/3/10 10:38
 */
@Data
public class BinLogMsg implements Serializable {

    /**  **/
    private static final long serialVersionUID = 1L;

    private List<JSONObject> data;

    private String database;

    private Long es;

    private Long id;

    private Boolean isDdl;

    private String table;

    private long ts;

    private String type;

    private List<JSONObject> mysqlType;

    private List<JSONObject> old;

    private JSONObject sqlType;

    private String sql;

    private List<String> pkNames;

}
