package com.fancy.retrofit_client.net.model;

import java.util.List;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public class BaseListCallModel<T> {
    private List<T> rows;
    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
