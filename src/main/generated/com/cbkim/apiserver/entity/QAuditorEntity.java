package com.cbkim.apiserver.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditorEntity is a Querydsl query type for AuditorEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAuditorEntity extends EntityPathBase<AuditorEntity> {

    private static final long serialVersionUID = -124572430L;

    public static final QAuditorEntity auditorEntity = new QAuditorEntity("auditorEntity");

    public final StringPath createBy = createString("createBy");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath updateBy = createString("updateBy");

    public final DateTimePath<java.time.LocalDateTime> updateDate = createDateTime("updateDate", java.time.LocalDateTime.class);

    public QAuditorEntity(String variable) {
        super(AuditorEntity.class, forVariable(variable));
    }

    public QAuditorEntity(Path<? extends AuditorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditorEntity(PathMetadata metadata) {
        super(AuditorEntity.class, metadata);
    }

}

