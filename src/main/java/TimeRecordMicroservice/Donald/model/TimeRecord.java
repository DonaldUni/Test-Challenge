package TimeRecordMicroservice.Donald.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class TimeRecord {

    @Id
    @GeneratedValue
    private Long id;

    /*@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", referencedColumnName = "id")*/
    //@AttributeOverrides({})
    @Embedded
    @AttributeOverride( name = "id", column = @Column(name = "userId"))
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime starting;

    @Column(nullable = false)
    private LocalTime ending;


    private transient float hours;

    @Column(nullable = false)
    private TimeType type;

    @Column(nullable = false)
    private boolean finalized;


    public TimeRecord() { }

    public TimeRecord(User user, LocalDate date, LocalTime starting, LocalTime ending, TimeType type, boolean finalized) {
        this.user = user;
        this.date = date;
        this.starting = starting;
        this.ending = ending;
        this.type = type;
        this.finalized = finalized;
        hours = calculateHours();
    }

    public TimeRecord(Long id, User user, LocalDate date, LocalTime starting, LocalTime ending, TimeType type, boolean finalized) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.starting = starting;
        this.ending = ending;
        this.type = type;
        this.finalized = finalized;
        hours = calculateHours();
    }


    private float calculateHours(){

        LocalTime starting = starting = LocalTime.now();
        LocalTime ending = starting;

        if (this.starting != null){
            starting = this.starting;
        }

        if (this.ending != null){
            ending = this.ending;
        }

        long tmpHour = Duration.between(starting, ending).toHours();
        long minutes = Duration.between(starting, ending).toMinutes();
        minutes = minutes - (tmpHour * 60); //  take the amount of minutes of the time
        String tmp = tmpHour +"."+ minutes; //  build a String of hour in Float format

        return Float.parseFloat(tmp);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStarting() {
        return starting;
    }


    public void setStarting(LocalTime starting) {
        this.starting = starting;
    }

    public LocalTime getEnding() {
        return ending;
    }

    public void setEnding(LocalTime ending) {
        this.ending = ending;
    }

    public float getHours() {
        return hours;
    }

    // the variable hours is transient, so it did not need any setter
    /*public void setHours(float hours) {
        this.hours = hours;
    }*/

    public TimeType getType() {
        return type;
    }

    public void setType(TimeType type) {
        this.type = type;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    @Override
    public String toString() {
        return "TimeRecord{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", starting=" + starting +
                ", ending=" + ending +
                ", hours=" + hours +
                ", type=" + type +
                ", finalized=" + finalized +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeRecord)) return false;
        TimeRecord that = (TimeRecord) o;
        return Float.compare(that.hours, hours) == 0 && finalized == that.finalized && id.equals(that.id) && user.equals(that.user) && date.equals(that.date) && starting.equals(that.starting) && ending.equals(that.ending) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date, starting, ending, hours, type, finalized);
    }
}
