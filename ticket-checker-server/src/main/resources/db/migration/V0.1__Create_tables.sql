CREATE TABLE "organization" (
    "id"                UUID NOT NULL,
    "owner_id"          UUID NULL,
    "name"              VARCHAR NOT NULL,
    "created_at"        TIMESTAMP NOT NULL
);

CREATE TABLE "organization_invite" (
    "id"                UUID NOT NULL,
    "organization_id"   UUID NOT NULL,
    "email"             VARCHAR NOT NULL,
    "code"              VARCHAR(10) NOT NULL,
    "status"            VARCHAR(10) NOT NULL,
    "responded_at"      TIMESTAMP NOT NULL,
    "invited_at"        TIMESTAMP NOT NULL
);

CREATE TABLE "organization_membership" (
    "id"                UUID NOT NULL,
    "user_id"           UUID NOT NULL,
    "invite_id"         UUID NOT NULL,
    "role"              VARCHAR NOT NULL,
    "joined_at"         TIMESTAMP NOT NULL
);

CREATE TABLE "user" (
    "id"                UUID NOT NULL,
    "organization_id"   UUID NOT NULL,
    "email"             VARCHAR UNIQUE NOT NULL,
    "hashed_password"   VARCHAR NOT NULL,
    "name"              VARCHAR NOT NULL,
    "created_at"        TIMESTAMP NOT NULL,
    "edited_at"         TIMESTAMP NULL
);

CREATE TABLE "ticket" (
    "id"                VARCHAR(255) NOT NULL,
    "organization_id"   UUID NOT NULL,
    "sold_to"           VARCHAR(100) NULL,
    "sold_to_birthday"  TIMESTAMP NULL,
    "sold_to_telephone" VARCHAR(10) NULL,
    "sold_by_id"        UUID NULL,
    "sold_by_name"      VARCHAR NOT NULL,
    "sold_at"           TIMESTAMP NOT NULL,
    "validated_by_id"   UUID NULL,
    "validated_by_name" VARCHAR NOT NULL,
    "validated_at"      TIMESTAMP NULL
);

ALTER TABLE "organization" ADD PRIMARY KEY ("id");
ALTER TABLE "organization_invite" ADD PRIMARY KEY ("id");
ALTER TABLE "organization_membership" ADD PRIMARY KEY ("id");
ALTER TABLE "user" ADD PRIMARY KEY ("id");
ALTER TABLE "ticket" ADD PRIMARY KEY ("id", "organization_id");

ALTER TABLE "organization" ADD CONSTRAINT organization_owner_id_fk FOREIGN KEY ("owner_id") REFERENCES "user" ("id") ON DELETE SET NULL;
ALTER TABLE "organization_invite" ADD CONSTRAINT organization_invite_organization_id_fk FOREIGN KEY ("organization_id") REFERENCES "organization" ("id") ON DELETE CASCADE;
ALTER TABLE "organization_membership" ADD CONSTRAINT organization_membership_user_id_fk FOREIGN KEY ("user_id") REFERENCES "user" ("id") ON DELETE CASCADE;
ALTER TABLE "organization_membership" ADD CONSTRAINT organization_membership_invite_id_fk FOREIGN KEY ("invite_id") REFERENCES "organization_invite" ("id") ON DELETE CASCADE;

ALTER TABLE "user" ADD CONSTRAINT user_organization_id_fk FOREIGN KEY ("organization_id") REFERENCES "organization" ("id") ON DELETE CASCADE;
ALTER TABLE "ticket" ADD CONSTRAINT ticket_organization_id_fk FOREIGN KEY ("organization_id") REFERENCES "organization" ("id") ON DELETE CASCADE;
ALTER TABLE "ticket" ADD CONSTRAINT ticket_sold_by_id_fk FOREIGN KEY ("sold_by_id") REFERENCES "user" ("id") ON DELETE SET NULL;
ALTER TABLE "ticket" ADD CONSTRAINT ticket_validated_by_id_fk FOREIGN KEY ("validated_by_id") REFERENCES "user" ("id") ON DELETE SET NULL;