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

    @GetMapping
    private List<TimeRecord> getAllTimeRecords(@RequestParam Optional<TimeType> type, @RequestParam Optional<Boolean> finalized){

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


        if (!isTypePresent && !isFinalisedPresent){

            resultTimeRecords = timeRecordServices.findAllTimeRecordsByExample(example);
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

    @GetMapping(path = "/of/{userId}")
    private List<TimeRecord> getTimeRecordsOfUser(@PathVariable(value = "userId")Long userId){

        return timeRecordServices.findTimeRecordsOfUser(userId);
    }

    @GetMapping(path = "/{id}")
    private TimeRecord getTimeRecord(@PathVariable(value = "id")Long id) throws ElementNotFoundException {

        return timeRecordServices.findTimeRecord(id);
    }


    @PostMapping
    private TimeRecord postNewTimeRecord( @RequestBody TimeRecord newTimeRecord) throws DuplicateException {

        if (newTimeRecord == null){

            return null;
        }else {

            return timeRecordServices.addTimeRecord(newTimeRecord);
        }
    }

    @PatchMapping(path ="/{id}/update")
    private TimeRecord updateTimeRecord(@PathVariable(name = "id") Long id, @RequestBody TimeRecord updatedTimeRecord) throws ElementNotFoundException {

        if (updatedTimeRecord == null){

            return null;
        }else {

            return timeRecordServices.updateTimeRecord(id, updatedTimeRecord);
        }
    }

    @PutMapping(path ="/{id}/finalize")
    private TimeRecord finalize(@PathVariable Long id) throws NotAllowedOperationException, DuplicateException, ElementNotFoundException {

        return timeRecordServices.finalise(id);
    }

    @PostMapping(path ="/vacation")
    private List<TimeRecord> requestVacation( @RequestParam(name = "userId") Long userId, @RequestBody Vacation vacation){

        if (vacation == null){

            return null ;
        }else {

            return timeRecordServices.addTimeRecordsForRequestedVacation(userId, vacation);
        }
    }

    @PatchMapping(path = "/vacation")
    private TimeRecord bookkeeperVacationTimeRecords(@RequestParam Long id, @RequestParam boolean status) throws ElementNotFoundException {

        if (status){

            return timeRecordServices.approveVacationTimeRecord(id, status);

        }else {

            return timeRecordServices.rejectVacationTimeRecord(id, status);
        }
    }

}
