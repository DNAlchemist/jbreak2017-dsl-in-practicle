package ru.jbreak
/**
 * @author Aleksey Dobrynin
 */
class SpockMacroTransformationTest extends GroovyTestCase {
    void testSimpleMock() {
        assertScript """
    interface Service {
    }

    def service = Mock(Service)

    assert service instanceof Service
"""
    }

    void testMockGetter() {
        assertScript """
    interface Service {
        Object getDefault()
        Integer getId()
        String getName()
    }

    def service = Mock(Service) {
        getId: { 
            println "123"; 
            return 1 
        }
        getName: { 
            println "123"; 
            return "Вася" 
        }
    }

    assert service.getDefault() == null
    assert service.getName() == "Вася"
    assert service.getId() == 1
"""
    }


    void testMockEquals() {
        assertScript """
    interface Service {
        boolean equals(Object obj)
    }

    def service = Mock(Service) {
        equals: { _ -> true }
    }

    assert service.equals(1) == true
"""
    }

}
