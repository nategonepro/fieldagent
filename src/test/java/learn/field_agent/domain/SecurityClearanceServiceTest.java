package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldFindElbows(){
        SecurityClearance sc = makeSC();
        when(repository.findById(1)).thenReturn(sc);
        SecurityClearance actual = service.findById(1);
        assertEquals(sc, actual);
    }

    @Test
    void shouldAdd(){
        SecurityClearance sc = new SecurityClearance(0, "Elbows Only");
        when(repository.add(sc)).thenReturn(makeSC());
        Result<SecurityClearance> result = service.add(sc);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(makeSC(), result.getPayload());
    }

    @Test
    void shouldNotAddWithSetId(){
        SecurityClearance sc = makeSC();
        sc.setSecurityClearanceId(1);
        Result<SecurityClearance> result = service.add(sc);
        assertFalse(result.isSuccess());
    }

    SecurityClearance makeSC(){
        SecurityClearance sc = new SecurityClearance(1, "Elbows Only");
        return sc;
    }
}