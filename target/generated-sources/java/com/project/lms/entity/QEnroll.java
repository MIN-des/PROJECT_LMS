package com.project.lms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEnroll is a Querydsl query type for Enroll
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QEnroll extends EntityPathBase<Enroll> {

    private static final long serialVersionUID = 2062457235L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEnroll enroll = new QEnroll("enroll");

    public final QCourse cId;

    public final NumberPath<Long> eId = createNumber("eId", Long.class);

    public final QStudent sId;

    public QEnroll(String variable) {
        this(Enroll.class, forVariable(variable), INITS);
    }

    public QEnroll(Path<? extends Enroll> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEnroll(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEnroll(PathMetadata metadata, PathInits inits) {
        this(Enroll.class, metadata, inits);
    }

    public QEnroll(Class<? extends Enroll> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cId = inits.isInitialized("cId") ? new QCourse(forProperty("cId"), inits.get("cId")) : null;
        this.sId = inits.isInitialized("sId") ? new QStudent(forProperty("sId")) : null;
    }

}

