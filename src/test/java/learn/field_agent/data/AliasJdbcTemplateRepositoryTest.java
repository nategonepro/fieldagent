package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }

    @Test
    void shouldAdd(){
        Alias alias = new Alias(0, "Habibi", "habibi habibi", 1);

        Alias actual = repository.add(alias);
        alias.setAliasId(4);

        assertNotNull(actual);
        assertEquals(alias, actual);
    }

    @Test
    void shouldUpdate(){
        Alias alias = new Alias(2, "James", "A boy with a giant peach", 1);
        assertTrue(repository.update(alias));
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.deleteById(3));
    }
}