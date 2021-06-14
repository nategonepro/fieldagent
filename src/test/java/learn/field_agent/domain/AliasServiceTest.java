package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository aliasRepository;

    @Test
    void shouldAdd(){
        Alias toAdd = new Alias(0,"Gerald","a straight up g(randpa)", 1);
        when(aliasRepository.add(toAdd)).thenReturn(makeAlias());
        Result<Alias> result = service.add(toAdd);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(makeAlias(), result.getPayload());
    }

    @Test
    void shouldNotAddWithSetId(){
        Alias alias = makeAlias();
        Result<Alias> result = service.add(alias);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNullAlias(){
        Result<Alias> result = service.add(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithMissingName(){
        Alias alias = makeAlias();
        alias.setName("");
        Result<Alias> result = service.add(alias);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdate(){
        Alias toUpdate = new Alias(1, "My Boy", "Just a simple lad", 1);

        when(aliasRepository.update(toUpdate)).thenReturn(true);
        Result<Alias> result = service.update(toUpdate);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateMissing(){
        Alias toUpdate = makeAlias();
        toUpdate.setAliasId(1500);

        when(aliasRepository.update(toUpdate)).thenReturn(false);
        Result<Alias> result = service.update(toUpdate);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid(){
        Alias toUpdate = new Alias(1, null, null, 1);

        Result<Alias> result = service.update(toUpdate);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenNull(){
        Alias toUpdate = null;

        Result<Alias> result = service.update(toUpdate);
        assertEquals(ResultType.INVALID, result.getType());
    }

    Alias makeAlias(){
        Alias alias = new Alias(1,"Gerald","a straight up g(randpa)",1);
        return alias;
    }

}