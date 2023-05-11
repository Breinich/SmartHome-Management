package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.type.Type;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommandDataService {

    private final CommandDataRepository commandDataRepository;

    @Autowired
    public CommandDataService(CommandDataRepository commandDataRepository) {
        this.commandDataRepository = commandDataRepository;
    }

    public List<ActuatorCommand> getDatas(){
        return commandDataRepository.findAll();
    }

    public void addNewData(ActuatorCommand command) {
       Optional<ActuatorCommand> dataOptional= commandDataRepository.findDataById(command.getDataId());
        if(dataOptional.isPresent()) {
            throw new IllegalStateException("id taken");
        }

        commandDataRepository.save(command);
    }

    public void deleteData(Integer dataId) {
        boolean exists = commandDataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalStateException("Data with id " + dataId + " does not exists");
        }
        commandDataRepository.deleteById(dataId);
    }


    @Transactional
    public void updateData(Integer dataId,Type premiseType, Integer premiseValue, Type consequenceType,
                           Integer consequenceValue, Date expiryDate) {
        ActuatorCommand command = commandDataRepository
                .findById(dataId).orElseThrow(() -> new IllegalStateException("data with id " + dataId + " does not exists"));

        if (premiseType == null)
            premiseType = command.getPremiseType();
        if (premiseValue == null)
            premiseValue = command.getPremiseValue();
        if (consequenceType == null)
            consequenceType = command.getConsequenceType();
        if (consequenceValue == null)
            consequenceValue = command.getConsequenceValue();
        if (expiryDate == null)
            expiryDate = command.getExpirationDate();

        command.setPremiseType(premiseType);
        command.setPremiseValue(premiseValue);
        command.setConsequenceType(consequenceType);
        command.setConsequenceValue(consequenceValue);
        command.setExpirationDate(expiryDate);
    }
}
