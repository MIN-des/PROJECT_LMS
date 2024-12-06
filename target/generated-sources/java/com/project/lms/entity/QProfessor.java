package com.project.lms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfessor is a Querydsl query type for Professor
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProfessor extends EntityPathBase<Professor> {

    private static final long serialVersionUID = 204147298L;

    public static final QProfessor professor = new QProfessor("professor");

    public final StringPath pAdd = createString("pAdd");

    public final StringPath pBirth = createString("pBirth");

    public final EnumPath<com.project.lms.constant.Dept> pDept = createEnum("pDept", com.project.lms.constant.Dept.class);

    public final StringPath pEmail = createString("pEmail");

    public final EnumPath<com.project.lms.constant.Gen> pGen = createEnum("pGen", com.project.lms.constant.Gen.class);

    public final StringPath pId = createString("pId");

    public final StringPath pName = createString("pName");

    public final StringPath pPw = createString("pPw");

    public final StringPath pTel = createString("pTel");

    public final EnumPath<com.project.lms.constant.Role> role = createEnum("role", com.project.lms.constant.Role.class);

    public final StringPath year = createString("year");

    public QProfessor(String variable) {
        super(Professor.class, forVariable(variable));
    }

    public QProfessor(Path<? extends Professor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfessor(PathMetadata metadata) {
        super(Professor.class, metadata);
    }

}

