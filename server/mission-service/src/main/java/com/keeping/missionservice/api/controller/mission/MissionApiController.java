package com.keeping.missionservice.api.controller.mission;

import com.keeping.missionservice.api.ApiResponse;
import com.keeping.missionservice.api.controller.mission.request.*;
import com.keeping.missionservice.api.controller.mission.response.MissionResponse;
import com.keeping.missionservice.api.service.mission.MissionService;
import com.keeping.missionservice.api.service.mission.dto.AddCommentDto;
import com.keeping.missionservice.api.service.mission.dto.AddMissionDto;
import com.keeping.missionservice.api.service.mission.dto.EditCompleteDto;
import com.keeping.missionservice.api.service.mission.dto.EditMissionDto;
import com.keeping.missionservice.global.exception.AlreadyExistException;
import com.keeping.missionservice.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mission-service/api/{member_key}")
@Slf4j
public class MissionApiController {

    private final MissionService missionService;

    /**
     * 미션 등록
     *
     * @param request 미션 request
     * @return 미션 식별키
     */
    @PostMapping
    public ApiResponse<Long> addMission(
            @PathVariable(name = "member_key") String memberKey,
            @Valid @RequestBody AddMissionRequest request
    ) {
        log.debug("addMission :: request={}", request);
        log.info("addMission :: request={}", request);

        AddMissionDto dto = AddMissionDto.toDto(request);

        try {
            Long missionId = missionService.addMission(memberKey, dto);
            return ApiResponse.ok(missionId);
        } catch (NotFoundException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }

    @GetMapping
    public ApiResponse<List<MissionResponse>> showMission(
            @PathVariable("member_key") String memberKey
    ) {
        List<MissionResponse> missionResponses = missionService.showMission(memberKey);
        return ApiResponse.ok(missionResponses);
    }

    @GetMapping("/{mission_id}")
    public ApiResponse<MissionResponse> showDetailMission(
            @PathVariable("member_key") String memberKey,
            @PathVariable("mission_id") Long missionId
    ) {
        try {
            MissionResponse response = missionService.showDetailMission(memberKey, missionId);
            return ApiResponse.ok(response);
        } catch (NotFoundException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }

    @PatchMapping
    public ApiResponse<Long> editMission(
            @PathVariable(name = "member_key") String memberKey,
            @Valid @RequestBody EditMissionRequest request
    ) {
        try {
            Long missionId = missionService.editMission(memberKey, EditMissionDto.toDto(request));
            return ApiResponse.ok(missionId);
        } catch (NotFoundException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }

    @PatchMapping("/comment")
    public ApiResponse<Long> addComment(
            @PathVariable(name = "member_key") String memberKey,
            @Valid @RequestBody AddCommentRequest request
            ) {
        try {
            Long missionId = missionService.addComment(memberKey, AddCommentDto.toDto(request));
            return ApiResponse.ok(missionId);
        } catch (NotFoundException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }

    @PatchMapping("/complete")
    public ApiResponse<Long> editComplete(
            @PathVariable(name = "member_key") String memberKey,
            @Valid @RequestBody EditCompleteRequest request
    ) {
        try {
            Long missionId = missionService.editCompleted(memberKey, EditCompleteDto.toDto(request));
            return ApiResponse.ok(missionId);
        } catch (NotFoundException | AlreadyExistException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }

    @DeleteMapping("/{mission_id}")
    public ApiResponse<Long> removeMission(
            @PathVariable(name = "member_key") String memberKey,
            @PathVariable(name = "mission_id") Long missionId
    ) {
        try {
            missionService.removeMission(memberKey, missionId);
            return ApiResponse.ok(missionId);
        } catch (NotFoundException e) {
            return ApiResponse.of(Integer.parseInt(e.getResultCode()), e.getHttpStatus(), e.getResultMessage(), null);
        }
    }
}
