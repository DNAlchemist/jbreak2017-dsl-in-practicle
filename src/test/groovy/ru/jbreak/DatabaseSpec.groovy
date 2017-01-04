package ru.jbreak

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
        result instanceof Database.Entity
    }

    def "Get simple sql string from missing method"() {
        when:
        def result = db "users" getByNameAndSex "Вася", "male"
        then:
        result == "SELECT * FROM users WHERE Name = Вася And Sex = male"
    }
}
