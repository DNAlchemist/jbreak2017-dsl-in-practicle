package ru.jbreak

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

/*
 * @author ruslanmikhalev, @date 12/30/16 7:41 PM
 */
class JdbcQueryExecutorSpec extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    String dbTestUrl;
    JdbcQueryExecutor db;

    def setup() {
        def destinationFile = new File(testProjectDir.newFolder(), "test.db");
        dbTestUrl = "jdbc:sqlite:${destinationFile.absolutePath}"
        db = new JdbcQueryExecutor(dbTestUrl)
    }

    def "Jdbc query execution result exists"() {
        setup:
        Sql.withInstance(dbTestUrl) { Sql sql ->
            sql.execute("CREATE TABLE users(name, password)")
            sql.execute("INSERT INTO users(name, password) VALUES('Homer', 'qwerty')")
            sql.execute("INSERT INTO users(name, password) VALUES('Lisa', 'qwerty')")
        }
        when:
        def result = db.select("users", [name:"Homer"])
        then:
        result == [[name:'Homer', password:'qwerty']] as List<GroovyRowResult>
    }

    def "Jdbc query update count is working correct"() {
        setup:
        Sql.withInstance(dbTestUrl) { Sql sql ->
            sql.execute("CREATE TABLE users(name, password)")
            sql.execute("INSERT INTO users(name, password) VALUES('Homer', 'qwerty')")
            sql.execute("INSERT INTO users(name, password) VALUES('Lisa', 'qwerty')")
        }
        when:
        def count = db.update("users", [name:'Homer'], [password:"123"])
        then:
        count == 1
    }

    def "Jdbc query update is working correct"() {
        setup:
        Sql.withInstance(dbTestUrl) { Sql sql ->
            sql.execute("CREATE TABLE users(name, password)")
            sql.execute("INSERT INTO users(name, password) VALUES('Homer', 'qwerty')")
            sql.execute("INSERT INTO users(name, password) VALUES('Lisa', 'qwerty')")
        }
        when:
        def result = null;
        db.update("users", [name:'Homer'], [password:"123"])
        Sql.withInstance(dbTestUrl) { Sql sql ->
            result = sql.rows("SELECT * FROM users")
        }
        then:
        result == [[name:'Homer', password:'123'], [name:'Lisa', password:'qwerty']]
    }

    def "Jdbc query insert is working correct"() {
        setup:
        Sql.withInstance(dbTestUrl, { Sql sql -> sql.execute("CREATE TABLE users(name, password)") })
        when:
        def result = null;
        db.insert("users", [name:'Homer', password:"123"])
        Sql.withInstance(dbTestUrl) { Sql sql ->
            result = sql.rows("SELECT * FROM users")
        }
        then:
        result == [[name:'Homer', password:'123']]
    }

}
