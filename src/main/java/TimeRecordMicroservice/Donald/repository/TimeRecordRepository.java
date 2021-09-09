package TimeRecordMicroservice.Donald.repository;

import TimeRecordMicroservice.Donald.model.TimeRecord;
import TimeRecordMicroservice.Donald.model.TimeType;
import TimeRecordMicroservice.Donald.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    //TimeRecord findByUserId(Long userId);

    List<TimeRecord> findAllByUserId(Long userId);

    List<TimeRecord> findAllByType(TimeType type);
}
