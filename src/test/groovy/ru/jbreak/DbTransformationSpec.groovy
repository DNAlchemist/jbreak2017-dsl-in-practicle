package ru.jbreak

import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by ruslanmikhalev on 08/01/17.
 */
@CompileStatic
class DbTransformationSpec extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    String dbTestUrl

    def setup() {
        def destinationFile = new File(testProjectDir.newFolder(), "test.db");
        dbTestUrl = "jdbc:sqlite:${destinationFile.absolutePath}"
    }

    def "Class annotated as Db should have db field with correct type"() {
        setup:
        def code = """
        package ru.jbreak;

        @${Db.name}("$dbTestUrl")
        class AnnotatedAsDb {

        }
        """.stripIndent()
        def invoker = new TransformTestHelper(new DbTransformation(), CompilePhase.SEMANTIC_ANALYSIS)
        def instance = invoker.parse(code).newInstance()

        when: def result = instance.hasProperty("db")
        then: result
        when: result = instance["db"]
        then: result instanceof Database
    }

    def "Db field accessor working with compile static mode"() {
        setup:
        def code = """
        package ru.jbreak;

        @${Db.name}("$dbTestUrl")
        class AnnotatedAsDb {

            @groovy.transform.CompileStatic
            def getDb() { this.db }

        }
        """.stripIndent()
        def invoker = new TransformTestHelper(new DbTransformation(), CompilePhase.SEMANTIC_ANALYSIS)
        when: invoker.parse(code).newInstance()
        then: noExceptionThrown()
    }

    def "Db field should provide connection string"() {
        setup:
        def code = """
        package ru.jbreak;

        @${Db.name}("$dbTestUrl")
        class AnnotatedAsDb {

        }
        """.stripIndent()
        def invoker = new TransformTestHelper(new DbTransformation(), CompilePhase.SEMANTIC_ANALYSIS)
        def instance = invoker.parse(code).newInstance()
        when:
        def result = instance["db"]["queryExecutor"]["url"]
        then:
        notThrown(MissingPropertyException)
        result == dbTestUrl
    }

}
