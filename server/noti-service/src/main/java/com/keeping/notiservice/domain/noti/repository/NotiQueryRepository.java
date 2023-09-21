package com.keeping.notiservice.domain.noti.repository;

import com.keeping.notiservice.api.controller.response.NotiResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.keeping.notiservice.domain.noti.QNoti.noti;
import static com.querydsl.core.types.Projections.constructor;

@Repository
public class NotiQueryRepository {
    
    private final JPAQueryFactory queryFactory;

    public NotiQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<NotiResponse> showNoti(String memberKey) {
        return queryFactory
                .select(constructor(NotiResponse.class,
                        noti.id,
                        noti.receptionKey,
                        noti.title,
                        noti.content,
                        noti.type,
                        noti.createdDate))
                .from(noti)
                .where(noti.receptionKey.eq(memberKey))
                .fetch();
    }

    public Optional<NotiResponse> showDetailNoti(String memberKey, Long notiId) {
        return Optional.ofNullable(queryFactory
                .select(constructor(NotiResponse.class,
                        noti.id,
                        noti.receptionKey,
                        noti.title,
                        noti.content,
                        noti.type,
                        noti.createdDate))
                .from(noti)
                .where(noti.receptionKey.eq(memberKey),
                        noti.id.eq(notiId))
                .fetchOne());
    }
    
}
