package com.example.bookjourneybackend.domain.comment.service;

import com.example.bookjourneybackend.domain.comment.domain.Comment;
import com.example.bookjourneybackend.domain.comment.domain.dto.response.CommentInfo;
import com.example.bookjourneybackend.domain.comment.domain.dto.response.GetCommentListResponse;
import com.example.bookjourneybackend.domain.comment.domain.repository.CommentLikeRepository;
import com.example.bookjourneybackend.domain.comment.domain.repository.CommentRepository;
import com.example.bookjourneybackend.domain.record.domain.Record;
import com.example.bookjourneybackend.domain.record.domain.repository.RecordLikeRepository;
import com.example.bookjourneybackend.domain.record.domain.repository.RecordRepository;
import com.example.bookjourneybackend.domain.record.dto.response.RecordInfo;
import com.example.bookjourneybackend.domain.user.domain.User;
import com.example.bookjourneybackend.domain.user.domain.repository.UserRepository;
import com.example.bookjourneybackend.global.exception.GlobalException;
import com.example.bookjourneybackend.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.bookjourneybackend.domain.record.domain.RecordType.PAGE;
import static com.example.bookjourneybackend.global.response.status.BaseExceptionResponseStatus.CANNOT_FOUND_RECORD;
import static com.example.bookjourneybackend.global.response.status.BaseExceptionResponseStatus.CANNOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final RecordLikeRepository recordLikeRepository;
    private final DateUtil dateUtil;

    @Transactional(readOnly = true)
    public GetCommentListResponse showComments(Long recordId, Long userId) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new GlobalException(CANNOT_FOUND_RECORD));
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException(CANNOT_FOUND_USER));

        List<Comment> comments = commentRepository.findByRecord(record);

        List<CommentInfo> commentList = comments.stream()
                .map(comment -> CommentInfo.from(comment, commentLikeRepository.existsByCommentAndUser(comment, user)))
                .toList();

        RecordInfo recordInfo;
        boolean isLiked = recordLikeRepository.existsByRecordAndUser(record, user);

        if (record.getRecordType() == PAGE) {
            recordInfo = RecordInfo.fromPageRecord(
                    record.getUser().getUserId(),
                    record.getRecordId(),
                    (record.getUser().getUserImage() != null) ? record.getUser().getUserImage().getImageUrl() : null,
                    record.getUser().getNickname(),
                    record.getRecordPage(),
                    dateUtil.formatLocalDateTime(record.getCreatedAt()),
                    record.getContent(),
                    record.getComments().size(),
                    record.getRecordLikes().size(),
                    isLiked
            );
        } else {
            recordInfo = RecordInfo.fromEntireRecord(
                    record.getUser().getUserId(),
                    record.getRecordId(),
                    (record.getUser().getUserImage() != null) ? record.getUser().getUserImage().getImageUrl() : null,
                    record.getUser().getNickname(),
                    record.getRecordTitle(),
                    dateUtil.formatLocalDateTime(record.getCreatedAt()),
                    record.getContent(),
                    record.getComments().size(),
                    record.getRecordLikes().size(),
                    isLiked
            );
        }

        return GetCommentListResponse.of(commentList, recordInfo);
    }
}
