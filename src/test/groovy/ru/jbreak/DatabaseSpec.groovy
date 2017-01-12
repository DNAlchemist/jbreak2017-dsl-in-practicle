package ru.jbreak

import groovy.sql.Sql
import groovy.transform.CompileStatic
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by ruslanmikhalev on 04/01/17.
 */
class DatabaseSpec extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    String dbTestUrl
    Database db

    @CompileStatic
    Model db(String arg) { db.call(arg) }

    @CompileStatic
    <T> T db(Class<T> arg) { db.call(arg) }

    @CompileStatic
    def setup() {
        def destinationFile = new File(testProjectDir.newFolder(), "test.db");
        dbTestUrl = "jdbc:sqlite:${destinationFile.absolutePath}"
        def queryExecutor = new JdbcQueryExecutor(dbTestUrl)
        db = new Database(queryExecutor)
    }

    @CompileStatic
    def "Get table from db returns EntityModel"() {
        when:
        def result = db "users"
        then:
        result instanceof Model
    }

    def "Get simple sql string from missing method"() {
        setup:
        Sql.withInstance(dbTestUrl) { Sql sql ->
            sql.execute("CREATE TABLE users(name, password)")
            sql.execute("INSERT INTO users(name, password) VALUES('Homer', 'qwerty')")
            sql.execute("INSERT INTO users(name, password) VALUES('Lisa', 'qwerty')")
        }
        when:
        def result = db "users" findByNameAndPassword "Homer", "qwerty"
        then:
        result == [[name:'Homer', password:'qwerty']]
    }

    @CompileStatic
    def "Class annotated as Entity have executor field"() {
        when:
        new User().executor
        then:
        noExceptionThrown()
    }

    @CompileStatic
    def "Class users from db have executor inside"() {
        when:
        def result = db User executor
        then:
        result instanceof QueryExecutor
    }
}
