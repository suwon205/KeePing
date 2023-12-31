package com.keeping.missionservice.api.service.mission.dto;

import com.keeping.missionservice.api.controller.mission.request.EditMissionRequest;
import com.keeping.missionservice.domain.mission.MissionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EditMissionDto {

    private Long missionId;

    private String todo; // 미션 내용

    private int money; // 미션 보상금

    private String cheeringMessage; // 부모의 응원 메시지

    private LocalDate startDate; // 미션 시작일

    private LocalDate endDate; // 미션 마감일

    @Builder
    public EditMissionDto(Long missionId, String todo, int money, String cheeringMessage, LocalDate startDate, LocalDate endDate) {
        this.missionId = missionId;
        this.todo = todo;
        this.money = money;
        this.cheeringMessage = cheeringMessage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Dto 객체로 변환
    public static EditMissionDto toDto(EditMissionRequest request) {
        return EditMissionDto.builder()
                .missionId(request.getMissionId())
                .todo(request.getTodo())
                .money(request.getMoney())
                .cheeringMessage(request.getCheeringMessage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
