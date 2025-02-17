package se.kth.felixhr.medical.records.search.model;

import java.sql.Date;

public class ConditionDTO {
    public Long conditionId;
    public Long patientId;
    public String conditionName;
    public String conditionInfo;
    public Date conditionDate;

    public ConditionDTO(Long conditionId, Long patientId, String conditionName, String conditionInfo, Date conditionDate) {
        this.conditionId = conditionId;
        this.patientId = patientId;
        this.conditionName = conditionName;
        this.conditionInfo = conditionInfo;
        this.conditionDate = conditionDate;
    }
}

