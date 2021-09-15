package TimeRecordMicroservice.Donald.services;

import TimeRecordMicroservice.Donald.exceptions.DuplicateException;
import TimeRecordMicroservice.Donald.exceptions.ElementNotFoundException;
import TimeRecordMicroservice.Donald.exceptions.NotAllowedOperationException;
import TimeRecordMicroservice.Donald.model.TimeRecord;
import TimeRecordMicroservice.Donald.model.TimeType;
import TimeRecordMicroservice.Donald.model.User;
import TimeRecordMicroservice.Donald.model.Vacation;
import TimeRecordMicroservice.Donald.repository.TimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<TimeRecord> addTimeRecordsForRequestedVacation(Long userId, Vacation vacation){

        LocalTime defaultStartHourAtWork = LocalTime.of(8,0);
        LocalTime defaultEndHourAtWork = LocalTime.parse("17:00"); // just to play with LocalTime Methods
        List<TimeRecord> timeRecordsOfVacation = new ArrayList<>();
        List<TimeRecord> savedTimeRecordsOfVacation = new ArrayList<>();

        //take Vacation Attribut
        LocalDate startDateOfVacation = vacation.getStartDate();
        LocalDate endDateOfVacation = vacation.getEndDate();
        LocalTime startingAt = vacation.getStartingAt();
        LocalTime endingAt = vacation.getEndingAt();
        boolean finalised = false;
        int startDayOfVacation = startDateOfVacation.getDayOfMonth();
        int endDayOfVacation  = endDateOfVacation.getDayOfMonth();


        int numberOfVacationDay = endDayOfVacation - startDayOfVacation + 1;

        for (int i = 0; i < numberOfVacationDay; i++) {

            TimeRecord timeRecord;
            User user = new User(userId);
            LocalDate date;

            if (i==0){
                date = startDateOfVacation;
                timeRecord = new TimeRecord(user, date, defaultStartHourAtWork,  startingAt, TimeType.ON_VACATION, finalised);
                timeRecordsOfVacation.add(timeRecord);
                continue;
            }

            if ( i < numberOfVacationDay-1){
                date = startDateOfVacation.plusDays(i);
                timeRecord = new TimeRecord(user, date, defaultStartHourAtWork,  defaultEndHourAtWork, TimeType.ON_VACATION, finalised);
                timeRecordsOfVacation.add(timeRecord);
                continue;
            }

            if ( i == numberOfVacationDay-1){
                date = endDateOfVacation;
                timeRecord = new TimeRecord(user, date, defaultStartHourAtWork,  endingAt, TimeType.ON_VACATION, finalised);
                timeRecordsOfVacation.add(timeRecord);

            }
        }

        for (TimeRecord timeRecord: timeRecordsOfVacation) {

            timeRecord = timeRecordRepository.save(timeRecord);
            savedTimeRecordsOfVacation.add(timeRecord);
        }

        return savedTimeRecordsOfVacation;
    }

    public TimeRecord approveVacationTimeRecord(Long id, boolean status) throws ElementNotFoundException {

        TimeRecord timeRecord1;

        Optional<TimeRecord> optionalTimeRecord = timeRecordRepository.findById(id);

        if (optionalTimeRecord.isPresent()){

            TimeRecord timeRecord = optionalTimeRecord.get();

            if (timeRecord.getType().equals(TimeType.ON_VACATION) && !timeRecord.isFinalized()){
                if (status){
                    timeRecord.setFinalized(true);
                    timeRecord1 = timeRecordRepository.save(timeRecord);
                    return timeRecord1;
                }
            }
         }else{

            throw new ElementNotFoundException(getErrorNotFoundMessage(id));
        }

        return null;
    }

    public TimeRecord rejectVacationTimeRecord(Long id, boolean status) throws ElementNotFoundException {

        Optional<TimeRecord> optionalTimeRecord = timeRecordRepository.findById(id);

        if (optionalTimeRecord.isPresent()){

            TimeRecord timeRecord = optionalTimeRecord.get();

            if (timeRecord.getType().equals(TimeType.ON_VACATION) && ! timeRecord.isFinalized()){
                if (!status){

                    timeRecordRepository.delete(timeRecord);
                    return timeRecord;
                }
            }
        }else{

            throw new ElementNotFoundException(getErrorNotFoundMessage(id));
        }

        return null;
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
