package TimeRecordMicroservice.Donald.services;

import TimeRecordMicroservice.Donald.exceptions.DuplicateException;
import TimeRecordMicroservice.Donald.exceptions.ElementNotFoundException;
import TimeRecordMicroservice.Donald.exceptions.NotAllowedOperationException;
import TimeRecordMicroservice.Donald.model.TimeRecord;
import TimeRecordMicroservice.Donald.model.TimeType;
import TimeRecordMicroservice.Donald.model.Vacation;
import TimeRecordMicroservice.Donald.repository.TimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeRecordServices {

    @Autowired
    private TimeRecordRepository timeRecordRepository;




    public List<TimeRecord> findAllTimeRecords(){

        return timeRecordRepository.findAll();
    }

    public List<TimeRecord> findAllTimeRecordsByExample(TimeRecord example){

        Example<TimeRecord> timeRecordExample = Example.of(example, ExampleMatcher.matchingAll());

        return timeRecordRepository.findAll(timeRecordExample);
    }

    public List<TimeRecord> findTimeRecordsOfUser(Long userId){

        return timeRecordRepository.findAllByUserId(userId);
    }

    public TimeRecord findTimeRecord(Long id) throws ElementNotFoundException{

        Optional<TimeRecord> optionalTimeRecord = timeRecordRepository.findById(id);

        return optionalTimeRecord.orElse(null);
    }

    public TimeRecord addTimeRecord(TimeRecord newTimeRecord) throws DuplicateException {

        if (newTimeRecord.getId() != null) {
            if (timeRecordRepository.existsById(newTimeRecord.getId())) {
                throw new DuplicateException(getErrorDuplicateMessage(newTimeRecord.getId()));
            }
        }

        newTimeRecord.setFinalized(false);
        return timeRecordRepository.save(newTimeRecord);
    }

    public TimeRecord updateTimeRecord(Long id, TimeRecord updatedTimeRecord) throws ElementNotFoundException {

        Optional<TimeRecord> optionalTimeRecord = timeRecordRepository.findById(id);
        TimeRecord savedUpdatedTimeRecord = null;

        if (optionalTimeRecord.isPresent()){

            TimeRecord timeRecord = optionalTimeRecord.get();
            if (timeRecord.getId() == updatedTimeRecord.getId()){

                if(! timeRecord.isFinalized()){

                    timeRecord.setDate(updatedTimeRecord.getDate());
                    timeRecord.setStarting(updatedTimeRecord.getStarting());
                    timeRecord.setEnding(updatedTimeRecord.getEnding());
                    timeRecord.setType(updatedTimeRecord.getType());

                }
                savedUpdatedTimeRecord = timeRecordRepository.save(timeRecord);
            }else{

                savedUpdatedTimeRecord = timeRecord;
            }
        }else{

            throw new ElementNotFoundException(getErrorNotFoundMessage(id));
        }

        return savedUpdatedTimeRecord;
    }

    public TimeRecord finalise(Long id) throws DuplicateException, ElementNotFoundException, NotAllowedOperationException {

        TimeRecord finalisedTimeRecord;

        Optional<TimeRecord> optionalTimeRecord = timeRecordRepository.findById(id);

        if (optionalTimeRecord.isPresent()){

            TimeRecord timeRecord = optionalTimeRecord.get();

            if (! timeRecord.getType().equals(TimeType.ON_VACATION)){
                timeRecord.setFinalized(true);
                finalisedTimeRecord = timeRecordRepository.save(timeRecord);
                return finalisedTimeRecord;
            }

            throw new NotAllowedOperationException(getErrorNotAllowedOperationMessage());

        }else{

            throw new ElementNotFoundException(getErrorNotFoundMessage(id));
        }
    }

    //TODO to fill in
    public List<TimeRecord> addTimeRecordsForRequestedVacation(Vacation vacation){

        timeRecordRepository.findAll();

        return null;
    }

    public List<TimeRecord> approveVacationTimeRecord(Long id){

        return null;
    }

    public void rejectVacationTimeRecord(Long id){


    }

    //Error Methode
    public String getErrorNotFoundMessage(Long id){

        return "This Timerecord with the id "+ id +" has been not found.";
    }

    public String getErrorDuplicateMessage(Long id){

        return "This Timerecord with the id "+ id +" alraidy exist.";
    }

    public String getErrorNotAllowedOperationMessage(){

        return "You are not allowed to do operation on Timerecord with Type ON_VACATION.";
    }





}
