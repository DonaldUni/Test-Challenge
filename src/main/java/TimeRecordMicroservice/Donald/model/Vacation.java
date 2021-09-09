package TimeRecordMicroservice.Donald.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Vacation {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startingAt;
    private LocalTime endingAt;


    public Vacation() { }

    public Vacation(LocalDate startDate, LocalDate endDate, LocalTime startingAt, LocalTime endingAt) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startingAt = startingAt;
        this.endingAt = endingAt;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartingAt() {
        return startingAt;
    }

    public void setStartingAt(LocalTime startingAt) {
        this.startingAt = startingAt;
    }

    public LocalTime getEndingAt() {
        return endingAt;
    }

    public void setEndingAt(LocalTime endingAt) {
        this.endingAt = endingAt;
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", startingAt=" + startingAt +
                ", endingAt=" + endingAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacation)) return false;
        Vacation vacation = (Vacation) o;
        return startDate.equals(vacation.startDate) && endDate.equals(vacation.endDate) && startingAt.equals(vacation.startingAt) && endingAt.equals(vacation.endingAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, startingAt, endingAt);
    }
}
