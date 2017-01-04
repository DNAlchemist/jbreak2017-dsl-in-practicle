package ru.jbreak

import groovy.transform.CompileStatic

/**
 * Created by ruslanmikhalev on 04/01/17.
 */
@CompileStatic
class Database {

    QueryExecutor queryExecutor

    public Database(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor
    }

    def call(String methodName) { new EntityModel(methodName) }

    public class EntityModel {
        String entityName;
        EntityModel(String entityName) {
            this.entityName = entityName
        }

        def methodMissing() {

        }
    }

}
