-- =====================================================================
-- V1 - Ilk sema
-- Bu dosya yayina alindiktan sonra ASLA degistirilmez.
-- Yeni bir degisiklik gerektiginde V2, V3 ... seklinde yeni dosya acilir.
-- =====================================================================

-- ---------------------------------------------------------------------
-- Sehirler
-- ---------------------------------------------------------------------
CREATE TABLE city (
    id          VARCHAR(10)  NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    plate_code  VARCHAR(5)   NOT NULL,
    map_x       DOUBLE PRECISION NOT NULL,
    map_y       DOUBLE PRECISION NOT NULL,
    sort_order  INTEGER      NOT NULL DEFAULT 0,
    CONSTRAINT pk_city PRIMARY KEY (id)
);

-- ---------------------------------------------------------------------
-- Birimler
-- ---------------------------------------------------------------------
CREATE TABLE unit (
    id       VARCHAR(20)  NOT NULL,
    name     VARCHAR(100) NOT NULL,
    city_id  VARCHAR(10)  NOT NULL,
    seq      INTEGER      NOT NULL DEFAULT 1,
    CONSTRAINT pk_unit PRIMARY KEY (id),
    CONSTRAINT fk_unit_city FOREIGN KEY (city_id) REFERENCES city (id)
);

CREATE INDEX ix_unit_city ON unit (city_id);

-- ---------------------------------------------------------------------
-- Personel
-- ---------------------------------------------------------------------
CREATE TABLE police (
    id                VARCHAR(20)  NOT NULL,
    badge_number      VARCHAR(20)  NOT NULL,
    full_name         VARCHAR(100) NOT NULL,
    age               INTEGER,
    police_rank       VARCHAR(50),
    score             INTEGER,
    photo_url         VARCHAR(200),
    phone             VARCHAR(20),
    start_date        DATE,
    city_id           VARCHAR(10)  NOT NULL,
    unit_id           VARCHAR(20)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    task_type         VARCHAR(20),
    daily_task_count  INTEGER      NOT NULL DEFAULT 0,
    daily_task_limit  INTEGER      NOT NULL DEFAULT 8,
    vehicle_plate     VARCHAR(20),
    CONSTRAINT pk_police PRIMARY KEY (id),
    CONSTRAINT uk_police_badge UNIQUE (badge_number),
    CONSTRAINT fk_police_city FOREIGN KEY (city_id) REFERENCES city (id),
    CONSTRAINT fk_police_unit FOREIGN KEY (unit_id) REFERENCES unit (id)
);

-- Listeler ve pano sorgulari bu alanlar uzerinden filtreleniyor
CREATE INDEX ix_police_city ON police (city_id);
CREATE INDEX ix_police_unit ON police (unit_id);
CREATE INDEX ix_police_status ON police (status);
CREATE INDEX ix_police_city_unit_status ON police (city_id, unit_id, status);

-- ---------------------------------------------------------------------
-- Araclar
-- ---------------------------------------------------------------------
CREATE TABLE vehicle (
    plate                  VARCHAR(20) NOT NULL,
    police_id              VARCHAR(20),
    brand                  VARCHAR(50),
    model                  VARCHAR(50),
    model_year             INTEGER,
    vehicle_type           VARCHAR(20),
    kilometers             INTEGER,
    last_maintenance_date  DATE,
    photo_url              VARCHAR(200),
    CONSTRAINT pk_vehicle PRIMARY KEY (plate),
    CONSTRAINT fk_vehicle_police FOREIGN KEY (police_id) REFERENCES police (id)
);

CREATE INDEX ix_vehicle_police ON vehicle (police_id);
CREATE INDEX ix_vehicle_type ON vehicle (vehicle_type);

-- ---------------------------------------------------------------------
-- Gorevler
-- ---------------------------------------------------------------------
CREATE TABLE task (
    id           VARCHAR(30)  NOT NULL,
    police_id    VARCHAR(20)  NOT NULL,
    city_id      VARCHAR(10)  NOT NULL,
    unit_id      VARCHAR(20)  NOT NULL,
    task_type    VARCHAR(20)  NOT NULL,
    location     VARCHAR(100),
    start_time   VARCHAR(5),
    end_time     VARCHAR(5),
    task_status  VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_task PRIMARY KEY (id),
    CONSTRAINT fk_task_police FOREIGN KEY (police_id) REFERENCES police (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_city FOREIGN KEY (city_id) REFERENCES city (id),
    CONSTRAINT fk_task_unit FOREIGN KEY (unit_id) REFERENCES unit (id)
);

CREATE INDEX ix_task_police ON task (police_id);
CREATE INDEX ix_task_city ON task (city_id);
CREATE INDEX ix_task_unit ON task (unit_id);
CREATE INDEX ix_task_type ON task (task_type);
CREATE INDEX ix_task_status ON task (task_status);

-- ---------------------------------------------------------------------
-- Raporlar
-- ---------------------------------------------------------------------
CREATE TABLE report (
    report_no     VARCHAR(20)  NOT NULL,
    report_name   VARCHAR(150) NOT NULL,
    report_type   VARCHAR(20)  NOT NULL,
    city_id       VARCHAR(10),
    unit_id       VARCHAR(20),
    city_name     VARCHAR(50),
    unit_name     VARCHAR(100),
    period        VARCHAR(50),
    police_count  INTEGER,
    task_count    INTEGER,
    created_date  DATE,
    CONSTRAINT pk_report PRIMARY KEY (report_no)
);

CREATE INDEX ix_report_type ON report (report_type);
CREATE INDEX ix_report_city ON report (city_id);

-- ---------------------------------------------------------------------
-- Kullanicilar
-- ---------------------------------------------------------------------
CREATE TABLE app_user (
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    full_name  VARCHAR(100),
    CONSTRAINT pk_app_user PRIMARY KEY (username)
);

-- ---------------------------------------------------------------------
-- Kullanici ayarlari
-- ---------------------------------------------------------------------
CREATE TABLE app_setting (
    token            VARCHAR(100) NOT NULL,
    language         VARCHAR(5)   NOT NULL DEFAULT 'tr',
    page_size        INTEGER      NOT NULL DEFAULT 20,
    default_city_id  VARCHAR(10),
    refresh_seconds  INTEGER      NOT NULL DEFAULT 0,
    CONSTRAINT pk_app_setting PRIMARY KEY (token)
);

-- ---------------------------------------------------------------------
-- Ekran yazilari (resource)
-- ---------------------------------------------------------------------
CREATE TABLE resource (
    id                BIGSERIAL    NOT NULL,
    transaction_name  VARCHAR(50)  NOT NULL,
    resource_key      VARCHAR(80)  NOT NULL,
    resource_value    VARCHAR(300) NOT NULL,
    CONSTRAINT pk_resource PRIMARY KEY (id),
    CONSTRAINT uk_resource UNIQUE (transaction_name, resource_key)
);

CREATE INDEX ix_resource_transaction ON resource (transaction_name);
