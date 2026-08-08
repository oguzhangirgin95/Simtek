-- =====================================================================
-- V7 - Ozellik bayraklari (feature flag)
--
-- Amac: prod'a cikmis bir surumde yarim kalan gelistirmelerin kapali
-- durabilmesi. Gelistirici kodu isEnableFeature('SIM-123') ile sarar;
-- ozellik yalnizca kaydin enabled = TRUE oldugu ortamda gorunur.
--
-- Ortam ayrimi ayri bir dosyayla degil, ayri veritabaniyla saglanir:
-- test, uat, preprod ve prod kendi veritabanina baglandigi icin ayni
-- kod farkli ortamlarda farkli acik/kapali olabilir.
--
-- Yeni ozellik acmak icin ilgili ortamin veritabaninda:
--   INSERT INTO feature_flag (code, description) VALUES ('SIM-123', 'Aciklama');
--   UPDATE feature_flag SET enabled = TRUE WHERE code = 'SIM-123';
-- =====================================================================

CREATE TABLE feature_flag (
    code        VARCHAR(50)  PRIMARY KEY,
    description VARCHAR(200),
    enabled     BOOLEAN      NOT NULL DEFAULT FALSE
);

COMMENT ON TABLE feature_flag IS 'Ortam bazli ozellik bayraklari; kod genelde JIRA numarasidir.';
COMMENT ON COLUMN feature_flag.code IS 'Ozellik numarasi, ornegin SIM-123.';
COMMENT ON COLUMN feature_flag.enabled IS 'Bu ortamda acik mi. Varsayilan kapali.';

-- Ornek kayit. Kapali baslar; acmak icin yukaridaki UPDATE kullanilir.
INSERT INTO feature_flag (code, description, enabled) VALUES
    ('SIM-000', 'Ornek bayrak. Yapiyi gostermek icin duruyor, silinebilir.', TRUE);
