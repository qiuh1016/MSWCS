package com.zhd.mswcs.moduls.setting.bean;

/**
 * Created by think-1 on 2017/11/20.
 */

public class EscalationCycleBean {
    private String id;
    private String time;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public EscalationCycleBean(String id, String time,boolean isSelected) {
        this.id = id;
        this.time = time;
        this.isSelected = isSelected;
    }
}
