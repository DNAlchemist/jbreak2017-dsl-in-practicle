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

    String dbTestUrl;
    Database db;

    def setup() {
        def destinationFile = new File(testProjectDir.newFolder(), "test.db");
        dbTestUrl = "jdbc:sqlite:${destinationFile.absolutePath}"
        def queryExecutor = new JdbcQueryExecutor(dbTestUrl)
        db = new Database(queryExecutor)
    }

    def "Creating table on left shift"() {
        when:
        def result = db "users"
        then:
        result
    }
}
