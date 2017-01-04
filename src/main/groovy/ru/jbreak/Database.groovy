package ru.jbreak

/**
 * Created by ruslanmikhalev on 04/01/17.
 */
class Database {

    QueryExecutor queryExecutor

    public Database(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor
    }

    def call(String entity) { new Entity(entity) }

    public class Entity {
        String name
        private final tokens = ["And", "Or"]

        Entity(String name) {
            this.name = name
        }

        def methodMissing(String methodName, args) {
            args = args.reverse() as List
            "SELECT * FROM $name WHERE " + (methodName["findBy".length()..-1] =~ /([A-Z][a-z0-9]*)/)
                    .collect {
                (tokens.contains(it[0])) ? it[0] : "${it[0]} = ${args.pop()}"
            }.join(" ")
        }
    }
}
