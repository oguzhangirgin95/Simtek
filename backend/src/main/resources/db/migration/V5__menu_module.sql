ALTER TABLE menu_item ADD COLUMN parent_code VARCHAR(50);

CREATE INDEX ix_menu_item_parent ON menu_item (parent_code);

INSERT INTO menu_item (code, title, path, sort_order) VALUES
    ('MENU_MONITORING', 'Izleme',    '', 10),
    ('MENU_PERSONNEL',  'Personel',  '', 20),
    ('MENU_VEHICLES',   'Araclar',   '', 30),
    ('MENU_OPERATIONS', 'Operasyon', '', 40),
    ('MENU_REGIONS',    'Bolgeler',  '', 50),
    ('MENU_UNITS',      'Birimler',  '', 60),
    ('MENU_REPORTS',    'Raporlar',  '', 70),
    ('MENU_ANALYTICS',  'Analiz',    '', 80),
    ('MENU_SETTINGS',   'Ayarlar',   '', 90);

UPDATE menu_item SET parent_code = 'MENU_MONITORING', sort_order = 10 WHERE code = 'MENU_DASHBOARD';
UPDATE menu_item SET parent_code = 'MENU_PERSONNEL',  sort_order = 10 WHERE code = 'MENU_POLICELIST';
UPDATE menu_item SET parent_code = 'MENU_VEHICLES',   sort_order = 10 WHERE code = 'MENU_VEHICLELIST';
UPDATE menu_item SET parent_code = 'MENU_OPERATIONS', sort_order = 10 WHERE code = 'MENU_TASKLIST';
UPDATE menu_item SET parent_code = 'MENU_OPERATIONS', sort_order = 20 WHERE code = 'MENU_TASKASSIGN';
UPDATE menu_item SET parent_code = 'MENU_REGIONS',    sort_order = 10 WHERE code = 'MENU_REGIONLIST';
UPDATE menu_item SET parent_code = 'MENU_UNITS',      sort_order = 10 WHERE code = 'MENU_UNITLIST';
UPDATE menu_item SET parent_code = 'MENU_REPORTS',    sort_order = 10 WHERE code = 'MENU_REPORTLIST';
UPDATE menu_item SET parent_code = 'MENU_REPORTS',    sort_order = 20 WHERE code = 'MENU_REPORTENTRY';
UPDATE menu_item SET parent_code = 'MENU_ANALYTICS',  sort_order = 10 WHERE code = 'MENU_TASKTREND';
UPDATE menu_item SET parent_code = 'MENU_SETTINGS',   sort_order = 10 WHERE code = 'MENU_PREFERENCES';

INSERT INTO resource (transaction_name, resource_key, resource_value) VALUES
    ('general', 'MENU_MONITORING', 'Izleme'),
    ('general', 'MENU_PERSONNEL',  'Personel'),
    ('general', 'MENU_VEHICLES',   'Araclar'),
    ('general', 'MENU_OPERATIONS', 'Operasyon'),
    ('general', 'MENU_REGIONS',    'Bolgeler'),
    ('general', 'MENU_UNITS',      'Birimler'),
    ('general', 'MENU_REPORTS',    'Raporlar'),
    ('general', 'MENU_ANALYTICS',  'Analiz'),
    ('general', 'MENU_SETTINGS',   'Ayarlar');
