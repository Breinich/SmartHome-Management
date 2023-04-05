package SmartHome.com.smarthome.Data;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Data> getDatas(){
        return dataRepository.findAll();
    }

    public void addNewData(Data data) {
       /* Optional<Data> dataOptional= dataRepository.findDataById(data.getDataId());
        if(dataOptional.isPresent()) {
            throw new IllegalStateException("id taken");
        }*/

        dataRepository.save(data);

    }

    public void deleteData(Integer dataId) {
        boolean exists = dataRepository.existsById(dataId);
        if(!exists){
            throw new IllegalStateException("Data with id " + dataId + " does not exists");
        }
        dataRepository.deleteById(dataId);
    }


    @Transactional
    public void updateData(Integer dataId) {
        Data data = dataRepository.findById(dataId).orElseThrow(() -> new IllegalStateException("data with id " + dataId + " does not exists"));

    }
}
