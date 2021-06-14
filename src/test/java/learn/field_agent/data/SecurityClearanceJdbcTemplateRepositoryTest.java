package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);
    }

    @Test
    void shouldNotFindByMissingId(){
        SecurityClearance actual = repository.findById(1500);
        assertNull(actual);
    }

    @Test
    void shouldFindAll(){
        List<SecurityClearance> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.size() >= 1);
    }

    @Test
    void shouldAdd(){
        SecurityClearance securityClearance = new SecurityClearance(0, "Eyes Only");

        SecurityClearance actual = repository.add(securityClearance);
        securityClearance.setSecurityClearanceId(4);

        assertNotNull(actual);
        assertEquals(securityClearance, actual);
    }

    @Test
    void shouldUpdate(){
        SecurityClearance sc = new SecurityClearance(2, "Elbows Only");

        assertTrue(repository.update(sc));
        assertEquals(sc, repository.findById(2));
    }

    @Test
    void shouldNotUpdateMissing(){
        SecurityClearance sc = new SecurityClearance(1500, "Nose Only");

        assertFalse(repository.update(sc));
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing(){
        assertFalse(repository.deleteById(1500));
    }
}