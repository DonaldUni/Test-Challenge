package TimeRecordMicroservice.Donald;

import TimeRecordMicroservice.Donald.exceptions.DuplicateException;
import TimeRecordMicroservice.Donald.model.TimeRecord;
import TimeRecordMicroservice.Donald.model.TimeType;
import TimeRecordMicroservice.Donald.model.User;
import TimeRecordMicroservice.Donald.services.TimeRecordServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

		//save the first object in Database
		var timeRecordService = applicationContext.getBean(TimeRecordServices.class);

		var user = new User(1L);
		var user1 = new User(2L);

		var date = LocalDate.now();
		var startingHour = LocalTime.now();
		var endingHour = LocalTime.now();
		var isNotFinalised = false;
		var timeRecord1 = new TimeRecord(user, date, startingHour, endingHour, TimeType.REGULAR_WORKTIME, isNotFinalised );
		var timeRecord2 = new TimeRecord(user, date, startingHour, endingHour, TimeType.REGULAR_WORKTIME, isNotFinalised );
		var timeRecord5 = new TimeRecord(user, date, startingHour, endingHour, TimeType.REGULAR_WORKTIME, isNotFinalised );
		var timeRecord3 = new TimeRecord(user, date, startingHour, endingHour, TimeType.OVERTIME, isNotFinalised );
		var timeRecord4 = new TimeRecord(user, date, startingHour, endingHour, TimeType.SICK, isNotFinalised );
		var timeRecord6 = new TimeRecord(user1, date, startingHour, endingHour, TimeType.ON_VACATION, isNotFinalised );
		var timeRecord7 = new TimeRecord(user1, date, startingHour, endingHour, TimeType.ON_VACATION, isNotFinalised );

		List<TimeRecord> timeRecords = new ArrayList<>();
		timeRecords.add(timeRecord1);
		timeRecords.add(timeRecord2);
		timeRecords.add(timeRecord3);
		timeRecords.add(timeRecord4);
		timeRecords.add(timeRecord5);
		timeRecords.add(timeRecord6);
		timeRecords.add(timeRecord7);



		timeRecords.forEach(timeRecord -> {
			try {
				timeRecordService.addTimeRecord(timeRecord);
			} catch (DuplicateException e) {
				e.printStackTrace();
			}
		});

	}

}
