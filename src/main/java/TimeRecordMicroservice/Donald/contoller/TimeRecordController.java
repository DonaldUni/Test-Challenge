package TimeRecordMicroservice.Donald.contoller;

import TimeRecordMicroservice.Donald.exceptions.DuplicateException;
import TimeRecordMicroservice.Donald.exceptions.ElementNotFoundException;
import TimeRecordMicroservice.Donald.exceptions.NotAllowedOperationException;
import TimeRecordMicroservice.Donald.model.TimeRecord;
import TimeRecordMicroservice.Donald.model.TimeType;
import TimeRecordMicroservice.Donald.model.Vacation;
import TimeRecordMicroservice.Donald.services.TimeRecordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/timeRecord")
public class TimeRecordController {

    @Autowired
    private TimeRecordServices timeRecordServices;

    @GetMapping  //TODO to fill in
    private List<TimeRecord> getAllTimeRecords(@RequestParam Optional<TimeType> type, @RequestParam Optional<Boolean> finalized){ // TODO verify the availability with Postman

        boolean isTypePresent = false;
        boolean isFinalisedPresent = false;
        TimeType inputType = null;
        List<TimeRecord> resultTimeRecords = null;
        TimeRecord example = new TimeRecord();

        if(type.isPresent()){
            isTypePresent = true;
            inputType = type.get();
        }
        if(finalized.isPresent()){
            isFinalisedPresent = true;
        }


        if (isTypePresent && isFinalisedPresent){

            example.setType(inputType);
            example.setFinalized(finalized.get());
            resultTimeRecords = timeRecordServices.findAllTimeRecordsByExample(example);
        }

        if (isTypePresent && !isFinalisedPresent){

            example.setType(inputType);
            resultTimeRecords = timeRecordServices.findAllTimeRecordsByExample(example);
        }
        if (!isTypePresent && isFinalisedPresent){

            example.setFinalized(finalized.get());
            resultTimeRecords = timeRecordServices.findAllTimeRecordsByExample(example);
        }

        return resultTimeRecords;
    }

    //TODO verify the availability with Postman, when the id do not exist, the result has to be null
    @GetMapping(path = "/of/{userId}")
    private List<TimeRecord> getTimeRecordsOfUser(@PathVariable(value = "userId")Long userId){
        /*List<TimeRecord> timeRecord = null;
        timeRecord = timeRecordServices.findTimeRecordsOfUser(userId);*/

        return timeRecordServices.findTimeRecordsOfUser(userId);
    }

    @GetMapping(path = "/{id}")
    private TimeRecord getTimeRecord(@PathVariable(value = "id")Long id) throws ElementNotFoundException {

        TimeRecord timeRecord = null;

        timeRecord = timeRecordServices.findTimeRecord(id);

        return timeRecord;
    }


    @PostMapping
    private TimeRecord postNewTimeRecord(@Valid @RequestBody TimeRecord newTimeRecord){

        TimeRecord savedTimeRecord = null;

        try {

            savedTimeRecord = timeRecordServices.addTimeRecord(newTimeRecord);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }

        return savedTimeRecord;
    }

    @PatchMapping(path ="/{id}/update")
    private TimeRecord updateTimeRecord(@PathVariable(name = "id") Long id, @RequestParam TimeRecord updatedTimeRecord) throws ElementNotFoundException {

        return timeRecordServices.updateTimeRecord(id, updatedTimeRecord);
    }

    @PutMapping(path ="/{id}/finalize")
    private TimeRecord finalize(@PathVariable Long id) throws NotAllowedOperationException, DuplicateException, ElementNotFoundException {

        return timeRecordServices.finalise(id);
    }

    @PostMapping(path ="/vacation")
    private List<TimeRecord> requestVacation(Vacation vacation){

        return timeRecordServices.addTimeRecordsForRequestedVacation(vacation);
    }

    //TODO  bookkeeperVacationTimeRecords to fill in
    @PatchMapping(path = "/vacation")
    private TimeRecord bookkeeperVacationTimeRecords(Long id, boolean status){

        if (status){

            timeRecordServices.approveVacationTimeRecord(id);

        }else {

            timeRecordServices.rejectVacationTimeRecord(id);
        }

        return null;
    }

}
