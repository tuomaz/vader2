package se.tuomas.vader2.domain;

import java.util.Date;

public class SensorSample {
    private String name;
    private String realName;
    private float value;
    private SensorType type;
    private Date timestamp;
    private Date updated;
    private boolean old;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public float getRoundedValue() {
        return (float) Math.round(value * 10) / 10;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(final SensorType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(final boolean old) {
        this.old = old;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(final String realName) {
        this.realName = realName;
    }

}
