CREATE TABLE user_info
(
    uuid         uuid primary key,
    created_by   varchar(255),
    created_when timestamp,
    updated_by   varchar(255),
    updated_when timestamp,
    is_active    boolean,
    user_id      uuid not null unique,
    company_id   uuid,
    employee_id  uuid
);