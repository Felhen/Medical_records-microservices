package se.kth.felixhr.medical.records.search.model;

import java.util.Date;

public class EncounterDTO {
    public Long encounterId;
    public Long patientId;
    public Long doctorId;
    public Date encounterDate;
    public String encounterInfo;
    public String patientName;

    public EncounterDTO(Long encounterId, Long patientId, Long doctorId, Date encounterDate, String encounterInfo, String patientName) {
        this.encounterId = encounterId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.encounterDate = encounterDate;
        this.encounterInfo = encounterInfo;
        this.patientName = patientName;
    }
}
