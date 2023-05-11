package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.room.Room;
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

    public void addNewCommand(ActuatorCommand command) {
       Optional<ActuatorCommand> dataOptional= commandDataRepository.findByDataid(command.getCommandId());
        if(dataOptional.isPresent()) {
            throw new IllegalStateException("id taken");
        }

        commandDataRepository.save(command);
    }

    public void deleteCommand(Integer dataId) {
        boolean exists = commandDataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalStateException("Data with id " + dataId + " does not exists");
        }
        commandDataRepository.deleteById(dataId);
    }


    @Transactional
    public void updateCommand(Room room, Integer dataId, Type premiseType, boolean greaterThan, Integer premiseValue, Type consequenceType,
                              Integer consequenceValue, Date startDate, Date expiryDate) {
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
        if (startDate == null)
            startDate = command.getStartDate();
        if (room == null)
            room = command.getRoom();

        command.setPremiseType(premiseType);
        command.setPremiseValue(premiseValue);
        command.setConsequenceType(consequenceType);
        command.setConsequenceValue(consequenceValue);
        command.setExpirationDate(expiryDate);
        command.setStartDate(startDate);
        command.setGreaterThan(greaterThan);
        command.setRoom(room);
    }
}
