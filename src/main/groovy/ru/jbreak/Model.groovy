package ru.jbreak

/**
 * @author Aleksey Dobrynin
 */
class Model {
    String name
    private final List tokens = ["And", "Or"]

    Model(String name) {
        this.name = name
    }

    def methodMissing(String methodName, args) {
        List params = args as List
        "SELECT * FROM $name WHERE " + (methodName["findBy".length()..-1] =~ /([A-Z][a-z0-9]*)/)
                .collect {
            (tokens.contains(it[0])) ? it[0] : "${it[0]} = ${params.pop()}"
        }.join(" ")
    }
}
