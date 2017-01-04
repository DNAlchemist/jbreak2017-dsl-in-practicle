package ru.jbreak

import groovy.sql.Sql
import groovy.util.logging.Slf4j

/*
 * @author ruslanmikhalev, @date 12/30/16 8:12 PM
 */
@Slf4j
public class JdbcQueryExecutor implements QueryExecutor {

    final String url;

    public JdbcQueryExecutor(String url) {
        this.url = url;
    }

    @Override
    public int update(String entity, Map<String, Object> criteria, Map<String, Object> updateValues) {
        assert criteria : "Criteria must not be empty"
        assert updateValues : "Update values must not be empty"
        int result = 0
        Sql.withInstance(url) { Sql sql ->
            result = sql.executeUpdate("UPDATE $entity SET "
                    + updateValues
                            .collect({ k, v -> "$k = ?"})
                            .join(", ")
                    + " WHERE "
                    + criteria
                            .collect({ k, v -> " $k = ?" })
                            .join(" AND"), (updateValues.values() + criteria.values()) as Object[])
        }
        return result
    }

    @Override
    public List<?> select(String entity, Map<String, Object> criteria) {
        assert criteria : "Criteria must not be empty"
        def result = null
        Sql.withInstance(url) { Sql sql ->
            result = sql.rows("SELECT * FROM $entity WHERE"
                    + criteria
                            .collect({ k, v -> " $k = ?" })
                            .join(" AND"), criteria.values() as Object[])
        }
        return result
    }

    @Override
    void insert(String entity, Map<String, Object> params) {
        Sql.withInstance(url) { Sql sql ->
            sql.executeInsert("\
                                INSERT INTO $entity(${params.keySet().join(", ")}) \
                                VALUES(${params.values().collect({"?"}).join(", ")})"
                                .stripIndent(), params.values() as Object[])
        }
    }
}
