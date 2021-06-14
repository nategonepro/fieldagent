package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository securityClearanceRepository;
    private final AgencyAgentRepository agencyAgentRepository;

    public SecurityClearanceService(SecurityClearanceRepository securityClearanceRepository, AgencyAgentRepository agencyAgentRepository) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    public List<SecurityClearance> findAll(){
        return securityClearanceRepository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId){
        return securityClearanceRepository.findById(securityClearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance){
        Result<SecurityClearance> result = validate(securityClearance);
        if(!result.isSuccess()){
            return result;
        }

        if(securityClearance.getSecurityClearanceId() != 0){
            result.addMessage("securityClearanceId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = securityClearanceRepository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance){
        Result<SecurityClearance> result = validate(securityClearance);
        if(!result.isSuccess()){
            return result;
        }

        if(securityClearance.getSecurityClearanceId() <= 0){
            result.addMessage("securityClearanceId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if(!securityClearanceRepository.update(securityClearance)){
            String msg = String.format("securityClearanceId: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<SecurityClearance> deleteById(int securityClearanceId){
        Result<SecurityClearance> result = new Result<>();

        if(isReferenced(securityClearanceId)){
            result.addMessage("security clearance cannot be deleted if referenced by agency_agent", ResultType.INVALID);
            return result;
        }

        boolean deleted = securityClearanceRepository.deleteById(securityClearanceId);
        if(!deleted){
            result.addMessage("security clearance id #" + securityClearanceId + " not found", ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance){
        Result<SecurityClearance> result = new Result<>();

        if(securityClearance == null){
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(securityClearance.getName())){
            result.addMessage("security clearance name is required.", ResultType.INVALID);
        }

        List<SecurityClearance> all = findAll();
        for(SecurityClearance sc : all){
            if(securityClearance.getName().equalsIgnoreCase(sc.getName())){
                result.addMessage("security clearance name cannot be duplicated.", ResultType.INVALID);
            }
        }

        return result;
    }

    private boolean isReferenced(int securityClearanceId){
        List<AgencyAgent> agencyAgents = agencyAgentRepository.findAll();

        for(AgencyAgent aa : agencyAgents){
            if(aa.getSecurityClearance().getSecurityClearanceId() == securityClearanceId){
                return true;
            }
        }

        return false;
    }
}
