package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
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

    @Test
    void shouldUpdate(){
        SecurityClearance toUpdate = new SecurityClearance(1, "Ears Only");

        when(repository.update(toUpdate)).thenReturn(true);
        Result<SecurityClearance> actual = service.update(toUpdate);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing(){
        SecurityClearance toUpdate = new SecurityClearance(1500, "Ultra Secret");

        when(repository.update(toUpdate)).thenReturn(false);
        Result<SecurityClearance> actual = service.update(toUpdate);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid(){
        SecurityClearance toUpdate = new SecurityClearance(1, null);

        Result<SecurityClearance> result = service.update(toUpdate);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenNull(){
        SecurityClearance toUpdate = null;

        Result<SecurityClearance> result = service.update(toUpdate);
        assertEquals(ResultType.INVALID, result.getType());
    }

    SecurityClearance makeSC(){
        SecurityClearance sc = new SecurityClearance(1, "Elbows Only");
        return sc;
    }
}