package com.keeping.missionservice.api.service.mission;

import com.keeping.missionservice.api.controller.mission.response.MissionResponse;
import com.keeping.missionservice.api.service.mission.dto.AddMissionDto;
import com.keeping.missionservice.api.service.mission.dto.EditMissionDto;
import com.keeping.missionservice.domain.mission.Completed;

import java.util.List;

public interface MissionService {

    public Long addMission(String memberId, AddMissionDto dto);
    
    public List<MissionResponse> showMission(String memberId);
    
    public MissionResponse showDetailMission(String memberId, Long missionId);

    public Long addComment(String memberId, Long missionId);

    public Long editCompleted(String memberId, Long missionId, Completed completed);

    public Long editMission(String memberId, EditMissionDto dto);

    public Long removeMission(String memberId, Long missionId);
}