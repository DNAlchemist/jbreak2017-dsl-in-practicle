package ru.jbreak

import groovy.transform.CompileStatic

/**
 * Created by ruslanmikhalev on 04/01/17.
 */
class Database {

    QueryExecutor queryExecutor

    public Database(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor
    }

    @CompileStatic
    Model call(String entity) { new Model(entity, queryExecutor) }

    def <T> T call(Class<T> clazz) {
        def entity = clazz.newInstance()
        entity.executor = queryExecutor
        return entity
    }
}
