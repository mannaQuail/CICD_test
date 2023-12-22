package com.hacker.hacker.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    VIDEOS_BY_CATEGORY_GET_SUCCESS(OK,"카테고리별 동영상 반환에 성공했습니다"),
    GET_VIDEOS_BY_ID_SUCCESS(OK, "개별 동영상 반환에 성공했습니다."),
    GET_ALL_VIDEOS_SUCCESS(OK, "전체 동영상 반환에 성공했습니다."),
    VIDEO_UPLOAD_SUCCESS(CREATED, "동영상 업로드 성공"),
    GET_USERS_BY_ID_SUCCESS(OK, "개별 유저 반환에 성공했습니다."),
    GET_ALL_USERS_SUCCESS(OK, "전체 유저 반환에 성공했습니다."),


    /**
     * transcripts
     */

    GET_TRANSCRIPTS_SUCCESS(OK, "자막 반환에 성공했습니다")
    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
