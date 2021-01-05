package platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
public class Snippet {

    @Id
    private UUID uuid = UUID.randomUUID();
    private String code;
    private LocalDateTime date = LocalDateTime.now();
    private LocalTime time;
    private int views;
    private boolean restrictTime;
    private boolean restrictView;

    public Snippet() {
    }

    @JsonIgnore
    public UUID getUuid() {
        return uuid;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;

        if (views > 0) {
            this.restrictView = true;
        }
    }

    public long getTime() {
        return restrictTime ? LocalTime.now().until(time, ChronoUnit.SECONDS) : 0;
    }

    public void setTime(long time) {
        this.time = LocalTime.now().plusSeconds(time);

        if (time > 0) {
            this.restrictTime = true;
        }
    }

    @JsonIgnore
    public boolean isRestrictTime() {
        return restrictTime;
    }

    @JsonIgnore
    public boolean isRestrictView() {
        return restrictView;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "uuid=" + uuid +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", views=" + views +
                ", restrictTime=" + restrictTime +
                ", restrictView=" + restrictView +
                '}';
    }
}
