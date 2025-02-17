package se.kth.felixhr.medical.records.search.model;

import java.util.Date;

public class EncounterDTO {
    public Long encounterId;
    public Long patientId;
    public Long doctorId;
    public Date encounterDate;
    public String encounterInfo;

    public EncounterDTO(Long encounterId, Long patientId, Long doctorId, Date encounterDate, String encounterInfo) {
        this.encounterId = encounterId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.encounterDate = encounterDate;
        this.encounterInfo = encounterInfo;
    }
}
