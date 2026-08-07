CREATE TABLE menu_item (
    id          BIGSERIAL    PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    title       VARCHAR(100) NOT NULL,
    path        VARCHAR(200) NOT NULL,
    sort_order  INTEGER      NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX ix_menu_item_sort ON menu_item (sort_order);

INSERT INTO menu_item (code, title, path, sort_order) VALUES
    ('MENU_DASHBOARD',   'Pano',            '/monitoring/dashboard/start',   10),
    ('MENU_POLICELIST',  'Personel',        '/personnel/policelist/start',   20),
    ('MENU_VEHICLELIST', 'Araclar',         '/vehicles/vehiclelist/start',   30),
    ('MENU_TASKLIST',    'Gorevler',        '/operations/tasklist/start',    40),
    ('MENU_TASKASSIGN',  'Gorev Atama',     '/operations/taskassign/start',  50),
    ('MENU_REGIONLIST',  'Sehirler',        '/regions/regionlist/start',     60),
    ('MENU_UNITLIST',    'Birimler',        '/units/unitlist/start',         70),
    ('MENU_REPORTLIST',  'Raporlar',        '/reports/reportlist/start',     80),
    ('MENU_REPORTENTRY', 'Rapor Girisi',    '/reports/reportentry/start',    90),
    ('MENU_TASKTREND',   'Analiz',          '/analytics/tasktrend/start',   100),
    ('MENU_PREFERENCES', 'Ayarlar',         '/settings/preferences/start',  110);

INSERT INTO resource (transaction_name, resource_key, resource_value) VALUES
    ('general', 'MENU_DASHBOARD',   'Pano'),
    ('general', 'MENU_POLICELIST',  'Personel'),
    ('general', 'MENU_VEHICLELIST', 'Araclar'),
    ('general', 'MENU_TASKLIST',    'Gorevler'),
    ('general', 'MENU_TASKASSIGN',  'Gorev Atama'),
    ('general', 'MENU_REGIONLIST',  'Sehirler'),
    ('general', 'MENU_UNITLIST',    'Birimler'),
    ('general', 'MENU_REPORTLIST',  'Raporlar'),
    ('general', 'MENU_REPORTENTRY', 'Rapor Girisi'),
    ('general', 'MENU_TASKTREND',   'Analiz'),
    ('general', 'MENU_PREFERENCES', 'Ayarlar'),
    ('general', 'MENU_TITLE',       'Menu');
