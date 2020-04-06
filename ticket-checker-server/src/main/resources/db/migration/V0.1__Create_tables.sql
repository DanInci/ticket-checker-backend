CREATE TABLE "organization" (
    "id"                UUID PRIMARY KEY,
    "name"              VARCHAR NOT NULL,
    "created_at"        TIMESTAMP NOT NULL
);

CREATE TABLE "user" (
    "id"                UUID PRIMARY KEY,
    "organization_id"   UUID REFERENCES "organization"(id) ON DELETE CASCADE NOT NULL,
    "email"             VARCHAR UNIQUE NOT NULL,
    "hashed_password"   VARCHAR NOT NULL,
    "name"              VARCHAR NOT NULL,
    "role"              VARCHAR NOT NULL,
    "created_at"        TIMESTAMP NOT NULL,
    "edited_at"         TIMESTAMP NULL
);

CREATE TABLE "ticket" (
    "id"                VARCHAR(255) PRIMARY KEY,
    "organization_id"   UUID REFERENCES "organization"(id) ON DELETE CASCADE NOT NULL,
    "sold_to"           VARCHAR(100) NULL,
    "sold_to_birthday"  TIMESTAMP NULL,
    "sold_to_telephone" VARCHAR(10) NULL,
    "sold_by_id"        UUID REFERENCES "user"(id) ON DELETE SET NULL NULL,
    "sold_by_name"      VARCHAR NOT NULL,
    "sold_at"           TIMESTAMP NOT NULL,
    "validated_by_id"   UUID REFERENCES "user"(id) ON DELETE SET NULL NULL,
    "validated_by_name" VARCHAR NOT NULL,
    "validated_at"      TIMESTAMP NULL
);