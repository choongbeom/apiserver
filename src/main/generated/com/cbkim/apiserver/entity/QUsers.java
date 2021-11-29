package com.cbkim.apiserver.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = 1146127225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsers users = new QUsers("users");

    public final QAuditorEntity _super = new QAuditorEntity(this);

    public final StringPath address = createString("address");

    public final StringPath addressSub = createString("addressSub");

    public final StringPath adminYn = createString("adminYn");

    public final StringPath birth = createString("birth");

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DateTimePath<java.time.LocalDateTime> deleteDate = createDateTime("deleteDate", java.time.LocalDateTime.class);

    public final QCodeManagement dormantConversion;

    public final StringPath email = createString("email");

    public final StringPath fcmYn = createString("fcmYn");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> height = createNumber("height", Long.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final SimplePath<Images> image = createSimple("image", Images.class);

    public final StringPath invitaionCode = createString("invitaionCode");

    public final DateTimePath<java.time.LocalDateTime> lastSignin = createDateTime("lastSignin", java.time.LocalDateTime.class);

    public final StringPath memo = createString("memo");

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath phone = createString("phone");

    public final StringPath phoneAuth = createString("phoneAuth");

    public final StringPath receiveMarketingEmailYn = createString("receiveMarketingEmailYn");

    public final StringPath receiveMarketingKakaotalkYn = createString("receiveMarketingKakaotalkYn");

    public final StringPath receiveMarketingSmsYn = createString("receiveMarketingSmsYn");

    public final StringPath recommentdationCode = createString("recommentdationCode");

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath signinDevice = createString("signinDevice");

    public final StringPath signinSession = createString("signinSession");

    public final QCodeManagement status;

    public final StringPath subscriptionPath = createString("subscriptionPath");

    public final StringPath termsConditionsYn = createString("termsConditionsYn");

    //inherited
    public final StringPath updateBy = _super.updateBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final StringPath usePersonalInformationYn = createString("usePersonalInformationYn");

    public final StringPath userCi = createString("userCi");

    public final StringPath userCode = createString("userCode");

    public final StringPath userDi = createString("userDi");

    public final StringPath userFb = createString("userFb");

    public final StringPath userId = createString("userId");

    public final StringPath userPw = createString("userPw");

    public final NumberPath<Long> weight = createNumber("weight", Long.class);

    public final StringPath zipcode = createString("zipcode");

    public QUsers(String variable) {
        this(Users.class, forVariable(variable), INITS);
    }

    public QUsers(Path<? extends Users> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsers(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsers(PathMetadata metadata, PathInits inits) {
        this(Users.class, metadata, inits);
    }

    public QUsers(Class<? extends Users> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dormantConversion = inits.isInitialized("dormantConversion") ? new QCodeManagement(forProperty("dormantConversion")) : null;
        this.status = inits.isInitialized("status") ? new QCodeManagement(forProperty("status")) : null;
    }

}

