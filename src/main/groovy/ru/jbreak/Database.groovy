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
    def call(String methodName) { new EntityModel(methodName) }

    def <T> T call(Class<T> entityClass) {
        def entity = entityClass.newInstance()
        entity.executor = queryExecutor
        return entity
    }

    @CompileStatic
    public class EntityModel {
        String entityName;
        EntityModel(String entityName) {
            this.entityName = entityName
        }

        def methodMissing() {

        }
    }

}
