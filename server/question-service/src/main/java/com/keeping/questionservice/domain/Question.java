package com.keeping.questionservice.domain;

import com.keeping.questionservice.global.common.TimeBaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends TimeBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    
    private String parentKey;
    
    private String childKey;
    
    private String content;

    @Column(name = "is_created")
    private boolean isCreated;

    @Column(name = "parent_answer")
    @Lob
    private String parentAnswer;

    @Column(name = "child_answer")
    @Lob
    private String childAnswer;

    private LocalDateTime scheduledTime;


    @Builder
    public Question(Long id, String parentKey, String childKey, String content, boolean isCreated, String parentAnswer, String childAnswer, LocalDateTime scheduledTime) {
        this.id = id;
        this.parentKey = parentKey;
        this.childKey = childKey;
        this.content = content;
        this.isCreated = isCreated;
        this.parentAnswer = parentAnswer;
        this.childAnswer = childAnswer;
        this.scheduledTime = scheduledTime;
    }

    public static Question toQuestion(String parentKey, String childKey, String content, boolean isCreated, LocalDateTime scheduledTime) {
        return Question.builder()
                .parentKey(parentKey)
                .childKey(childKey)
                .content(content)
                .isCreated(isCreated)
                .scheduledTime(scheduledTime)
                .build();
    }

    public void updateQuestion(String content){
        this.content = content;
    }


    public void updateParentAnswer(String answer) {
        this.parentAnswer = answer;
    }

    public void updateChildAnswer(String answer) {
        this.childAnswer = answer;
    }

}
