package com.project.lms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = 2140743874L;

    public static final QAdmin admin = new QAdmin("admin");

    public final StringPath aId = createString("aId");

    public final StringPath aName = createString("aName");

    public final StringPath aPw = createString("aPw");

    public final EnumPath<com.project.lms.constant.Role> role = createEnum("role", com.project.lms.constant.Role.class);

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

