package com.cbkim.apiserver.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeManagement is a Querydsl query type for CodeManagement
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCodeManagement extends EntityPathBase<CodeManagement> {

    private static final long serialVersionUID = -1126115393L;

    public static final QCodeManagement codeManagement = new QCodeManagement("codeManagement");

    public final QAuditorEntity _super = new QAuditorEntity(this);

    public final StringPath code = createString("code");

    public final StringPath codeValue = createString("codeValue");

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath description = createString("description");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath parentCode = createString("parentCode");

    //inherited
    public final StringPath updateBy = _super.updateBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QCodeManagement(String variable) {
        super(CodeManagement.class, forVariable(variable));
    }

    public QCodeManagement(Path<? extends CodeManagement> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeManagement(PathMetadata metadata) {
        super(CodeManagement.class, metadata);
    }

}

