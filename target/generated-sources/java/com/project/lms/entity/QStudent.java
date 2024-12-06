package com.project.lms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStudent is a Querydsl query type for Student
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStudent extends EntityPathBase<Student> {

    private static final long serialVersionUID = -773974290L;

    public static final QStudent student = new QStudent("student");

    public final NumberPath<Integer> grade = createNumber("grade", Integer.class);

    public final EnumPath<com.project.lms.constant.Role> role = createEnum("role", com.project.lms.constant.Role.class);

    public final StringPath sAdd = createString("sAdd");

    public final StringPath sBirth = createString("sBirth");

    public final EnumPath<com.project.lms.constant.Dept> sDept = createEnum("sDept", com.project.lms.constant.Dept.class);

    public final StringPath sEmail = createString("sEmail");

    public final EnumPath<com.project.lms.constant.Gen> sGen = createEnum("sGen", com.project.lms.constant.Gen.class);

    public final StringPath sId = createString("sId");

    public final StringPath sName = createString("sName");

    public final StringPath sPw = createString("sPw");

    public final StringPath sTel = createString("sTel");

    public QStudent(String variable) {
        super(Student.class, forVariable(variable));
    }

    public QStudent(Path<? extends Student> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStudent(PathMetadata metadata) {
        super(Student.class, metadata);
    }

}

