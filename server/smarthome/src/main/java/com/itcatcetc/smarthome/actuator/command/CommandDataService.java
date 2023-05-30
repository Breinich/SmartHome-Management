package com.itcatcetc.smarthome.actuator.command;

import com.itcatcetc.smarthome.room.Room;
import com.itcatcetc.smarthome.type.Type;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CommandDataService
 * provides methods to access the database
 * specifying the business logic
 */
@Service
@PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
public class CommandDataService {
    private final CommandDataRepository commandDataRepository;

    /**
     * constructor
     * @param commandDataRepository autowired by Spring
     */
    @Autowired
    public CommandDataService(CommandDataRepository commandDataRepository) {
        this.commandDataRepository = commandDataRepository;
    }

    /**
     * get the list of all commands from the database
     * @return list of commands
     */
    public List<ActuatorCommand> getData() {
        return commandDataRepository.findAll();
    }

    /**
     * add a new coomand to the database
     */
    public void addNewCommand(ActuatorCommand command) {
        Optional<ActuatorCommand> dataOptional = commandDataRepository.findByCommandId(command.getCommandId());
        if (dataOptional.isPresent()) {
            throw new IllegalArgumentException("id taken");
        }

        if (command.getStartDate() == null)
            command.setStartDate(command.getTimestamp());

        commandDataRepository.save(command);
    }

    /**
     * delete a command from the database
     * @param dataId the id of the command to delete
     */
    public void deleteCommand(Integer dataId) {
        boolean exists = commandDataRepository.existsById(dataId);
        if (!exists) {
            throw new IllegalArgumentException("Data with id " + dataId + " does not exists");
        }
        commandDataRepository.deleteById(dataId);
    }


    /**
     * update a command in the database
     * @param newCommand the new command to update
     */
    @Transactional
    public void updateCommand(ActuatorCommand newCommand) {

        Integer dataId = newCommand.getCommandId();
        Type premiseType = newCommand.getPremiseType();
        Integer premiseValue = newCommand.getPremiseValue();
        Type consequenceType = newCommand.getConsequenceType();
        Integer consequenceValue = newCommand.getConsequenceValue();
        Date expiryDate = newCommand.getExpirationDate();
        Date startDate = newCommand.getStartDate();
        boolean greaterThan = newCommand.isGreaterThan();
        Room room = newCommand.getRoom();

        ActuatorCommand command = commandDataRepository
                .findById(dataId).orElseThrow(() -> new IllegalArgumentException("data with id " + dataId + " does not exists"));

        if (premiseType == null)
            premiseType = command.getPremiseType();
        if (premiseValue == null)
            premiseValue = command.getPremiseValue();
        if (consequenceType == null)
            consequenceType = command.getConsequenceType();
        if (expiryDate == null)
            expiryDate = command.getExpirationDate();
        if (startDate == null)
            startDate = command.getStartDate();

        command.setPremiseType(premiseType);
        command.setPremiseValue(premiseValue);
        command.setConsequenceType(consequenceType);
        command.setConsequenceValue(consequenceValue);
        command.setExpirationDate(expiryDate);
        command.setStartDate(startDate);
        command.setGreaterThan(greaterThan);
        command.setRoom(room);
    }

    public List<ActuatorCommand> getActiveCommandsByRoomIdAndType(Integer roomId, Type type) {
        return commandDataRepository.findByRoomRoomIdAndPremiseTypeAndExpirationDateAfter(roomId, type,new Date(System.currentTimeMillis()));
    }
}
