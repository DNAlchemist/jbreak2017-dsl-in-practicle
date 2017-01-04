package ru.jbreak

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

    def setup() {
        def destinationFile = new File(testProjectDir.newFolder(), "test.db");
        dbTestUrl = "jdbc:sqlite:${destinationFile.absolutePath}"
        def queryExecutor = new JdbcQueryExecutor(dbTestUrl)
        db = new Database(queryExecutor)
    }

    def "Get table from db returns EntityModel"() {
        when:
        def result = db "users"
        then:
        result instanceof Model
    }

    def "Get simple sql string from missing method"() {
        when:
        def result = db "users" findBySexAndName "male", "Вася"
        then:
        result == "SELECT * FROM users WHERE Sex = male And Name = Вася"
    }

    def "Class annotated as Entity have executor field"() {
        when:
        new User().executor
        then:
        noExceptionThrown()
    }

    def "Class users from db have executor inside"() {
        when:
        def result = db User executor
        println result
        then:
        result
    }

    @Entity
    @CompileStatic
    public static class User<T> {
        def name;
    }
}
