package ru.jbreak;

import java.util.List;
import java.util.Map;

/*
 * @author ruslanmikhalev, @date 12/30/16 7:41 PM
 */
interface QueryExecutor {
    int update(String entity, Map<String, Object> id, Map<String, Object> updateValues);
    List<?> select(String entity, Map<String, Object> criteria);
}
