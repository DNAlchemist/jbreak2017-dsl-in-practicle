package ru.jbreak

/**
 * @author Aleksey Dobrynin
 */
class Model {
    String name
    QueryExecutor executor
    private final List tokens = ["And", "Or"]

    Model(String name, QueryExecutor executor) {
        this.name = name
        this.executor = executor
    }

    def methodMissing(String methodName, args) {
        List params = args as List
        def criteria = (methodName["findBy".length()..-1] =~ /([A-Z][a-z0-9]*)/)
                .findAll { !tokens.contains(it[0]) }
                .inject([:]) { map, col -> map << [(col[0]): params.pop()] }

        executor.select(name, criteria)
    }
}
