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

    String dbTestUrl;
    Database db;

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
        result instanceof Database.EntityModel
    }

    def "Class annotated as Entity have executor field"() {
        when:
        new Users().executor
        then:
        noExceptionThrown()
    }

    def "Class users from db have executor inside"() {
        when:
        def result = db Users executor
        println result
        then:
        result
    }

    @Entity
    @CompileStatic
    public class Users<T> {
        def name;
    }
}
