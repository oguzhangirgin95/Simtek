-- =====================================================================
-- V2 - Sabit veriler: sehirler, birimler, kullanicilar, ekran yazilari
-- =====================================================================

-- ---------------------------------------------------------------------
-- Sehirler. map_x / map_y gercek koordinatlardan 0-100 / 0-60 araligina tasindi.
-- ---------------------------------------------------------------------
INSERT INTO city (id, name, plate_code, map_x, map_y, sort_order) VALUES
    ('34', 'Istanbul',   '34', 15.70,  9.90,  1),
    ('06', 'Ankara',     '06', 36.10, 20.70,  2),
    ('35', 'Izmir',      '35',  6.00, 35.80,  3),
    ('16', 'Bursa',      '16', 16.10, 18.10,  4),
    ('07', 'Antalya',    '07', 24.80, 51.00,  5),
    ('01', 'Adana',      '01', 49.10, 50.00,  6),
    ('42', 'Konya',      '42', 34.10, 41.30,  7),
    ('27', 'Gaziantep',  '27', 59.90, 49.30,  8),
    ('38', 'Kayseri',    '38', 49.90, 32.70,  9),
    ('55', 'Samsun',     '55', 54.40,  7.10, 10),
    ('61', 'Trabzon',    '61', 72.20, 10.00, 11),
    ('21', 'Diyarbakir', '21', 74.90, 40.90, 12),
    ('25', 'Erzurum',    '25', 80.40, 21.00, 13),
    ('65', 'Van',        '65', 91.50, 35.10, 14);

-- ---------------------------------------------------------------------
-- Birimler. Ankara ilce birimleriyle, diger sehirler standart birimlerle acilir.
-- ---------------------------------------------------------------------
INSERT INTO unit (id, name, city_id, seq) VALUES
    ('06-B1', 'Cankaya Trafik Denetleme',     '06', 1),
    ('06-B2', 'Kecioren Trafik Denetleme',    '06', 2),
    ('06-B3', 'Yenimahalle Trafik Denetleme', '06', 3),
    ('06-B4', 'Etimesgut Trafik Denetleme',   '06', 4),
    ('06-B5', 'Mamak Trafik Denetleme',       '06', 5),
    ('06-B6', 'Bolge Trafik Denetleme',       '06', 6);

INSERT INTO unit (id, name, city_id, seq)
SELECT c.id || '-B1', 'Merkez Trafik Denetleme', c.id, 1 FROM city c WHERE c.id <> '06'
UNION ALL
SELECT c.id || '-B2', 'Bolge Trafik Denetleme', c.id, 2 FROM city c WHERE c.id <> '06'
UNION ALL
SELECT c.id || '-B3', 'Otoyol Denetleme', c.id, 3 FROM city c WHERE c.id <> '06';

-- ---------------------------------------------------------------------
-- Kullanicilar
-- ---------------------------------------------------------------------
INSERT INTO app_user (username, password, full_name) VALUES
    ('admin', 'admin',  'Sistem Yoneticisi'),
    ('oguz',  '1234',   'Oguzhan Girgin'),
    ('mehmet','1234',   'Mehmet Yilmaz');

-- ---------------------------------------------------------------------
-- Ekran yazilari
-- ---------------------------------------------------------------------
INSERT INTO resource (transaction_name, resource_key, resource_value) VALUES
    -- tum ekranlarda ortak
    ('general', 'APP_TITLE',            'Simtek'),
    ('general', 'BUTTON_CONTINUE',      'Devam'),
    ('general', 'BUTTON_BACK',          'Geri'),
    ('general', 'BUTTON_CLEAR',         'Temizle'),
    ('general', 'BUTTON_SAVE',          'Kaydet'),
    ('general', 'BUTTON_DELETE',        'Sil'),
    ('general', 'BUTTON_LOGOUT',        'Cikis'),
    ('general', 'LOADING',              'Yukleniyor...'),
    ('general', 'CONFIRM_TITLE',        'Onay'),
    ('general', 'EXECUTE_TITLE',        'Sonuc'),
    ('general', 'FILTER_CITY',          'Sehir'),
    ('general', 'FILTER_UNIT',          'Birim'),
    ('general', 'FILTER_STATUS',        'Durum'),
    ('general', 'FILTER_ALL',           'Tumu'),
    ('general', 'FILTER_TYPE',          'Arac tipi'),
    ('general', 'FILTER_TASKTYPE',      'Gorev tipi'),
    ('general', 'FILTER_LIMIT',         'Limit durumu'),
    ('general', 'GRID_BADGE',           'Sicil'),
    ('general', 'GRID_FULLNAME',        'Ad Soyad'),
    ('general', 'GRID_RANK',            'Rutbe'),
    ('general', 'GRID_CITY',            'Sehir'),
    ('general', 'GRID_UNIT',            'Birim'),
    ('general', 'GRID_STATUS',          'Durum'),
    ('general', 'GRID_TASKTYPE',        'Gorev'),
    ('general', 'GRID_SCORE',           'Puan'),
    ('general', 'GRID_LOCATION',        'Konum'),
    ('general', 'GRID_STARTTIME',       'Baslangic'),
    ('general', 'GRID_ENDTIME',         'Bitis'),
    ('general', 'GRID_TOTALPOLICE',     'Personel'),
    ('general', 'GRID_ACTIVEPOLICE',    'Sahada'),
    ('general', 'GRID_PATROL',          'Devriye'),
    ('general', 'GRID_RADAR',           'Radar'),
    ('general', 'GRID_MOTORCYCLE',      'Motosiklet'),
    ('general', 'GRID_TASKLOAD',        'Gorev'),
    ('general', 'GRID_TASKCOUNT',       'Gorev'),
    ('general', 'DETAIL_AGE',           'Yas'),
    ('general', 'DETAIL_PHONE',         'Telefon'),
    ('general', 'DETAIL_STARTDATE',     'Goreve baslama'),
    ('general', 'DETAIL_DAILYTASK',     'Gunluk gorev'),
    ('general', 'DETAIL_PLATE',         'Plaka'),
    ('general', 'DETAIL_TYPE',          'Tip'),
    ('general', 'DETAIL_BRAND',         'Marka'),
    ('general', 'DETAIL_MODEL',         'Model'),
    ('general', 'DETAIL_KM',            'Kilometre'),
    ('general', 'DETAIL_MAINTENANCE',   'Son bakim'),

    -- login
    ('login', 'LOGIN_TITLE',    'Giris'),
    ('login', 'LOGIN_USERNAME', 'Kullanici adi'),
    ('login', 'LOGIN_PASSWORD', 'Sifre'),
    ('login', 'LOGIN_BUTTON',   'Giris yap'),
    ('login', 'LOGIN_ERROR',    'Kullanici adi veya sifre hatali.'),

    -- dashboard
    ('dashboard', 'DASHBOARD_TITLE',      'Trafik Polisi Takip Panosu'),
    ('dashboard', 'STAT_TOTAL',           'Toplam personel'),
    ('dashboard', 'STAT_ONDUTY',          'Sahada'),
    ('dashboard', 'STAT_ATSTATION',       'Merkezde'),
    ('dashboard', 'STAT_ONLEAVE',         'Izinde'),
    ('dashboard', 'STAT_ONREPORT',        'Raporlu'),
    ('dashboard', 'STAT_OVERLIMIT',       'Limiti asan'),
    ('dashboard', 'MAP_TITLE',            'Sehir bazli aktif memur'),
    ('dashboard', 'STATUS_CHART_TITLE',   'Durum dagilimi'),
    ('dashboard', 'UNIT_WORKLOAD_TITLE',  'birim gorev yogunlugu'),
    ('dashboard', 'EMPTY_UNIT',           'Birim bulunamadi'),
    ('dashboard', 'GRID_UNIT',            'Birim'),
    ('dashboard', 'GRID_TOTALPOLICE',     'Personel'),
    ('dashboard', 'GRID_ACTIVEPOLICE',    'Sahada'),
    ('dashboard', 'GRID_PATROL',          'Devriye'),
    ('dashboard', 'GRID_RADAR',           'Radar'),
    ('dashboard', 'GRID_MOTORCYCLE',      'Motosiklet'),
    ('dashboard', 'GRID_TASKLOAD',        'Gorev'),

    -- policelist
    ('policelist', 'POLICELIST_TITLE',     'Personel Listesi'),
    ('policelist', 'POLICELIST_SEARCH',    'Ad veya sicil'),
    ('policelist', 'POLICELIST_LIST',      'Personel'),
    ('policelist', 'POLICELIST_HINT',      'Detay icin satira tiklayin'),
    ('policelist', 'POLICELIST_DETAIL',    'Detay'),
    ('policelist', 'POLICELIST_SELECT',    'Listeden bir personel secin.'),
    ('policelist', 'POLICELIST_OVERLIMIT', 'Bu personel gunluk gorev limitini asmis.'),
    ('policelist', 'POLICELIST_NOVEHICLE', 'Bu personele tanimli arac yok.'),
    ('policelist', 'POLICELIST_EMPTY',     'Kayit bulunamadi'),
    ('policelist', 'POLICELIST_EMPTYTASK', 'Gorev kaydi yok'),
    ('policelist', 'TAB_PERSON',           'Kisi'),
    ('policelist', 'TAB_VEHICLE',          'Arac'),
    ('policelist', 'TAB_TASK',             'Gorevler'),

    -- vehiclelist
    ('vehiclelist', 'VEHICLELIST_TITLE',     'Arac Envanteri'),
    ('vehiclelist', 'VEHICLELIST_SEARCH',    'Plaka veya marka'),
    ('vehiclelist', 'VEHICLELIST_LIST',      'Araclar'),
    ('vehiclelist', 'VEHICLELIST_HINT',      'Detay icin satira tiklayin'),
    ('vehiclelist', 'VEHICLELIST_DETAIL',    'Arac detayi'),
    ('vehiclelist', 'VEHICLELIST_SELECT',    'Listeden bir arac secin.'),
    ('vehiclelist', 'VEHICLELIST_TYPECHART', 'Tip dagilimi'),
    ('vehiclelist', 'VEHICLELIST_TOTAL',     'Listelenen arac'),
    ('vehiclelist', 'VEHICLELIST_EMPTY',     'Arac bulunamadi'),
    ('vehiclelist', 'GRID_PLATE',            'Plaka'),
    ('vehiclelist', 'GRID_TYPE',             'Tip'),
    ('vehiclelist', 'GRID_BRAND',            'Marka'),
    ('vehiclelist', 'GRID_MODEL',            'Model'),
    ('vehiclelist', 'GRID_MODELYEAR',        'Yil'),
    ('vehiclelist', 'GRID_KM',               'Km'),
    ('vehiclelist', 'GRID_ASSIGNED',         'Zimmetli'),

    -- tasklist
    ('tasklist', 'TASKLIST_TITLE',         'Gorev Takibi'),
    ('tasklist', 'TASKLIST_TOTAL',         'Listelenen gorev'),
    ('tasklist', 'TASKLIST_LIST',          'Gorevler'),
    ('tasklist', 'TASKLIST_TYPECHART',     'Gorev tipi dagilimi'),
    ('tasklist', 'TASKLIST_EMPTY',         'Gorev bulunamadi'),
    ('tasklist', 'TASKLIST_ONLYOVERLIMIT', 'Limiti asanlar'),
    ('tasklist', 'TASKSTATUS_DONE',        'Tamamlandi'),
    ('tasklist', 'TASKSTATUS_ACTIVE',      'Devam ediyor'),
    ('tasklist', 'TASKSTATUS_PLANNED',     'Planlandi'),

    -- taskassign
    ('taskassign', 'TASKASSIGN_TITLE',    'Gorev Atama'),
    ('taskassign', 'TASKASSIGN_FORM',     'Gorev bilgileri'),
    ('taskassign', 'TASKASSIGN_HINT',     'Devam ile onay ekranina gecilir.'),
    ('taskassign', 'TASKASSIGN_POLICE',   'Personel'),
    ('taskassign', 'TASKASSIGN_LOCATION', 'Gorev yeri'),

    -- unitlist
    ('unitlist', 'UNITLIST_TITLE',       'Birim Yogunlugu'),
    ('unitlist', 'UNITLIST_LIST',        'Birimler'),
    ('unitlist', 'UNITLIST_TOTALTASK',   'Toplam gorev'),
    ('unitlist', 'UNITLIST_UNITCOUNT',   'Birim sayisi'),
    ('unitlist', 'UNIT_WORKLOAD_TITLE',  'birim gorev yogunlugu'),
    ('unitlist', 'EMPTY_UNIT',           'Birim bulunamadi'),
    ('unitlist', 'GRID_SCHOOL',          'Okul Gecidi'),
    ('unitlist', 'GRID_ACCIDENT',        'Kaza'),
    ('unitlist', 'GRID_LOADPERCENT',     'Yogunluk %'),

    -- regionlist
    ('regionlist', 'REGIONLIST_TITLE',   'Sehir Bazli Dagilim'),
    ('regionlist', 'REGIONLIST_LIST',    'Sehirler'),
    ('regionlist', 'REGIONLIST_HINT',    'Haritada secmek icin satira tiklayin'),
    ('regionlist', 'REGIONLIST_BUSIEST', 'En yogun sehir'),
    ('regionlist', 'REGIONLIST_EMPTY',   'Sehir bulunamadi'),
    ('regionlist', 'MAP_TITLE',          'Sehir bazli aktif memur'),
    ('regionlist', 'STAT_TOTAL',         'Toplam personel'),
    ('regionlist', 'STAT_ONDUTY',        'Sahada'),
    ('regionlist', 'GRID_PLATECODE',     'Plaka'),
    ('regionlist', 'GRID_ACTIVEPERCENT', 'Aktiflik %'),
    ('regionlist', 'GRID_UNITCOUNT',     'Birim'),

    -- tasktrend
    ('tasktrend', 'TASKTREND_TITLE',     'Gorev Analizi'),
    ('tasktrend', 'TASKTREND_CHART',     'Gunluk gorev trendi'),
    ('tasktrend', 'TASKTREND_SCOPE',     'Kapsam'),
    ('tasktrend', 'TASKTREND_TOTAL',     'Toplam gorev'),
    ('tasktrend', 'TASKTREND_AVERAGE',   'Gunluk ortalama'),
    ('tasktrend', 'TASKLIST_TYPECHART',  'Gorev tipi dagilimi'),
    ('tasktrend', 'GRID_DAY',            'Gun'),
    ('tasktrend', 'GRID_TASKCOUNT',      'Gorev'),

    -- reportentry
    ('reportentry', 'REPORTENTRY_TITLE',     'Rapor Girisi'),
    ('reportentry', 'REPORTENTRY_FORM',      'Rapor bilgileri'),
    ('reportentry', 'REPORTENTRY_HINT',      'Devam ile kapsam ozetine gecilir.'),
    ('reportentry', 'REPORTENTRY_NAME',      'Rapor adi'),
    ('reportentry', 'REPORTENTRY_TYPE',      'Rapor tipi'),
    ('reportentry', 'REPORTENTRY_STARTDATE', 'Baslangic tarihi'),
    ('reportentry', 'REPORTENTRY_ENDDATE',   'Bitis tarihi'),

    -- reportlist
    ('reportlist', 'REPORTLIST_TITLE',   'Olusturulan Raporlar'),
    ('reportlist', 'REPORTLIST_LIST',    'Raporlar'),
    ('reportlist', 'REPORTLIST_TOTAL',   'Rapor sayisi'),
    ('reportlist', 'REPORTLIST_EMPTY',   'Henuz rapor olusturulmadi'),
    ('reportlist', 'REPORTLIST_NEW',     'Yeni rapor'),
    ('reportlist', 'REPORTENTRY_TYPE',   'Rapor tipi'),
    ('reportlist', 'GRID_REPORTNO',      'Rapor No'),
    ('reportlist', 'GRID_REPORTNAME',    'Rapor adi'),
    ('reportlist', 'GRID_PERIOD',        'Donem'),
    ('reportlist', 'GRID_CREATEDDATE',   'Tarih'),

    -- preferences
    ('preferences', 'PREFERENCES_TITLE',       'Ayarlar'),
    ('preferences', 'PREFERENCES_FORM',        'Kullanici tercihleri'),
    ('preferences', 'PREFERENCES_LANGUAGE',    'Dil'),
    ('preferences', 'PREFERENCES_PAGESIZE',    'Sayfa basina kayit'),
    ('preferences', 'PREFERENCES_DEFAULTCITY', 'Varsayilan sehir'),
    ('preferences', 'PREFERENCES_REFRESH',     'Otomatik yenileme (sn)'),
    ('preferences', 'LANGUAGE_TR',             'Turkce'),
    ('preferences', 'LANGUAGE_EN',             'Ingilizce');
