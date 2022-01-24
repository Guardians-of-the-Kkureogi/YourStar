package com.ssafy.yourstar.domain.meeting.controller;

import com.ssafy.yourstar.domain.meeting.db.entity.Meeting;
import com.ssafy.yourstar.domain.meeting.request.MeetingApplyByStarPostReq;
import com.ssafy.yourstar.domain.meeting.request.MeetingApplyByUserPostReq;
import com.ssafy.yourstar.domain.meeting.response.MeetingDetailGetRes;
import com.ssafy.yourstar.domain.meeting.response.MeetingListGetRes;
import com.ssafy.yourstar.domain.meeting.service.MeetingService;
import com.ssafy.yourstar.global.model.response.BaseResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("팬미팅 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @ApiOperation(value = "스타가 팬미팅 신청") // 이미지 등록하는 api 필요
    @PostMapping("/room-applicant")
    public ResponseEntity<? extends BaseResponseBody> meetingApplyByStar
            (@RequestBody MeetingApplyByStarPostReq meetingApplyByStarPostReq) {
        log.info("meetingApplyByStar - Call");
        log.info(meetingApplyByStarPostReq.toString());

        meetingService.meetingApplyByStar(meetingApplyByStarPostReq);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
    }

    @ApiOperation(value = "스타가 팬미팅 수정")
    @PutMapping("/room-applicant")
    public ResponseEntity<? extends BaseResponseBody> meetingModifyByStar
            (@RequestBody Meeting meeting) {
        log.info("meetingModifyByStar - Call");

        Meeting modifiedMeeting = meetingService.meetingModifyByStar(meeting);
        if (modifiedMeeting == null) {
            log.error("meetingModifyByStar - This MeetingId doesn't exist");
            return ResponseEntity.status(400).body(BaseResponseBody.of(400, "This MeetingId doesn't exist"));
        } else {
            return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
        }
    }

    @ApiOperation(value = "스타가 신청한 팬미팅 취소")
    @DeleteMapping("/room-applicant/{meetingId}")
    public ResponseEntity<? extends BaseResponseBody> meetingRemoveByStar
            (@ApiParam(value = "팬미팅 번호") @PathVariable("meetingId") int meetingId) {
        log.info("meetingRemoveByStar - Call");

        if (meetingService.meetingRemoveByStar(meetingId)) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        } else {
            log.error("meetingRemoveByStar - This MeetingId doesn't exist");
            return ResponseEntity.status(400).body(BaseResponseBody.of(400, "This MeetingId doesn't exist"));
        }
    }

    @ApiOperation(value = "팬미팅 전체보기")
    @GetMapping("/room-applicant")
    public ResponseEntity<MeetingListGetRes> meetingList(int page, int size) {
        log.info("meetingList - Call");

        Page<Meeting> meetingPage = meetingService.meetingList(PageRequest.of(page - 1, size));

        return ResponseEntity.status(200).body(MeetingListGetRes.of(200, "Success", meetingPage));
    }

    @ApiOperation(value = "승인 대기 중인 팬미팅 전체보기")
    @GetMapping("/room-applicant/pending")
    public ResponseEntity<MeetingListGetRes> meetingPendingList(int page, int size) {
        // 쿼리문으로 page와 size를 보내주면 해당하는 결과값만 리턴
        // ex) http://localhost:8080/api/meetings/room-applicant/pending?page=1&size=5

        log.info("meetingPendingList - Call");

        Page<Meeting> meetingPage = meetingService.meetingPendingList(PageRequest.of(page - 1, size));

        return ResponseEntity.status(200).body(MeetingListGetRes.of(200, "Success", meetingPage));
    }

    @ApiOperation(value = "팬미팅 승인")
    @GetMapping("/room-applicant/pending/{meetingId}")
    public ResponseEntity<? extends BaseResponseBody> meetingPendingApprove(@ApiParam(value = "팬미팅 번호") @PathVariable int meetingId) {
        log.info("meetingPendingApprove - Call");

        if (meetingService.meetingPendingApprove(meetingId)) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        } else {
            log.error("meetingPendingApprove - This MeetingId doesn't exist");
            return ResponseEntity.status(400).body(BaseResponseBody.of(400, "This MeetingId doesn't exist"));
        }
    }

    @ApiOperation(value = "승인된 팬미팅 전체보기")
    @GetMapping("/room-applicant/approve")
    public ResponseEntity<MeetingListGetRes> meetingApproveList(int page, int size) {
        log.info("meetingApproveList - Call");

        Page<Meeting> meetingPage = meetingService.meetingApproveList(PageRequest.of(page - 1, size));

        return ResponseEntity.status(200).body(MeetingListGetRes.of(200, "Success", meetingPage));
    }



    @ApiOperation(value = "팬미팅 상세보기")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDetailGetRes> meetingDetail(@ApiParam(value = "팬미팅 번호") @PathVariable int meetingId) {
        log.info("meetingDetail - Call");

        Meeting meeting = meetingService.meetingDetail(meetingId);

        if (meeting == null) {
            log.error("meetingDetail - This MeetingId doesn't exist");
            return ResponseEntity.status(400).body(MeetingDetailGetRes.of(400, "This MeetingId doesn't exist", null));
        } else {
            return ResponseEntity.status(200).body(MeetingDetailGetRes.of(200, "Success", meeting));
        }
    }

    @ApiOperation(value = "팬이 팬미팅 신청")
    @PostMapping("/fan-applicant")
    public ResponseEntity<? extends BaseResponseBody> meetingApplyByUser
            (@RequestBody MeetingApplyByUserPostReq meetingApplyByUserPostReq) {
        log.info("meetingApplyByUser - Call");

        meetingService.meetingApplyByUser(meetingApplyByUserPostReq);

        return ResponseEntity.status(201).body(BaseResponseBody.of(201, "Success"));
    }

    @ApiOperation(value = "팬이 신청한 팬미팅 취소")
    @DeleteMapping("/fan-applicant/{memberId}/{meetingId}")
    public ResponseEntity<? extends BaseResponseBody> meetingRemoveByUser
            (@ApiParam(value = "회원 구분 번호") @PathVariable("memberId") int memberId,
             @ApiParam(value = "팬미팅 번호") @PathVariable("meetingId") int meetingId) {
        log.info("meetingRemoveByUser - Call");

        if (meetingService.meetingRemoveByUser(memberId, meetingId)) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        } else {
            log.error("meetingRemoveByUser - This MeetingId or MemberId doesn't exist");
            return ResponseEntity.status(400).body(BaseResponseBody.of(400, "This MeetingId or MemberId doesn't exist"));
        }
    }

    @ApiOperation(value = "팬이 팬미팅 내역 확인")
    @GetMapping("/fan-applicant/{memberId}")
    public ResponseEntity<MeetingListGetRes> meetingApplyListByUser
            (@ApiParam(value = "회원 구분 번호") @PathVariable("memberId") int memberId, int page, int size) {
        log.info("meetingApplyListByUser - Call");

        Page<Meeting> meetingPage = meetingService.meetingApplyListByUser(memberId, PageRequest.of(page - 1, size));

        return ResponseEntity.status(200).body(MeetingListGetRes.of(200, "Success", meetingPage));
    }
}