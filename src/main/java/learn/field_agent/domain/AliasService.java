package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

@Service
public class AliasService {
    private final AliasRepository aliasRepository;
    private final AgentRepository agentRepository;

    public AliasService(AliasRepository aliasRepository, AgentRepository agentRepository) {
        this.aliasRepository = aliasRepository;
        this.agentRepository = agentRepository;
    }

    public Result<Alias> add(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() != 0){
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        Agent agent = agentRepository.findById(alias.getAgentId());

        if(agent == null){
            result.addMessage("agent does not exist", ResultType.INVALID);
            return result;
        }

        for(Alias al : agent.getAliases()){
            if(al.getName().equalsIgnoreCase(alias.getName())){
                if(Validations.isNullOrBlank(alias.getPersona())){
                    result.addMessage("persona is required if agent already has an alias with the same name", ResultType.INVALID);
                    return result;
                }
            }
        }

        alias = aliasRepository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() <= 0){
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        Agent agent = agentRepository.findById(alias.getAgentId());
        if(agent == null){
            result.addMessage("agent does not exist", ResultType.INVALID);
            return result;
        }

        if(!aliasRepository.update(alias)){
            String msg = String.format("aliasId: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId){
        return aliasRepository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias){
        Result<Alias> result = new Result<>();
        if(alias == null){
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(alias.getName())){
            result.addMessage("alias name is required", ResultType.INVALID);
        }

        return result;
    }
}
