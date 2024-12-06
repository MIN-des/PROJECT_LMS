package com.project.lms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFiles is a Querydsl query type for Files
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFiles extends EntityPathBase<Files> {

    private static final long serialVersionUID = 2145509354L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFiles files = new QFiles("files");

    public final QBoard bno;

    public final StringPath fName = createString("fName");

    public final StringPath fPath = createString("fPath");

    public final StringPath uuid = createString("uuid");

    public QFiles(String variable) {
        this(Files.class, forVariable(variable), INITS);
    }

    public QFiles(Path<? extends Files> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFiles(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFiles(PathMetadata metadata, PathInits inits) {
        this(Files.class, metadata, inits);
    }

    public QFiles(Class<? extends Files> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bno = inits.isInitialized("bno") ? new QBoard(forProperty("bno")) : null;
    }

}

