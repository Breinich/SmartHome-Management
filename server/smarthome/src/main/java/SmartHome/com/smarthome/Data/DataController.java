package SmartHome.com.smarthome.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/smarthome/data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public List<Data> getDatas(){
        return dataService.getDatas();
    }

    @PostMapping
    public void registerNewData(@RequestBody  Data data){dataService.addNewData(data);
    }

    @DeleteMapping(path = "{dataId}")
    public void deleteData(@PathVariable("dataId") Integer dataId){
        dataService.deleteData(dataId);
    }

    @PutMapping(path = "{dataId}")
    public void updateData(@PathVariable("dataId") Integer dataId, @RequestParam(required = false) Integer value){
        dataService.updateData(dataId);
    }
}
