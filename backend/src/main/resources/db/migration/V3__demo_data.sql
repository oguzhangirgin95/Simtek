-- =====================================================================
-- V3 - Ornek personel / arac / gorev verisi
-- Uretim deterministiktir: her calistirmada ayni kayitlar olusur.
-- =====================================================================

DO $$
DECLARE
    first_names   TEXT[] := ARRAY['Ahmet','Mehmet','Mustafa','Ali','Hasan','Huseyin','Emre','Burak',
                                  'Elif','Zeynep','Ayse','Fatma','Merve','Selin','Kemal','Onur'];
    last_names    TEXT[] := ARRAY['Yilmaz','Kaya','Demir','Sahin','Celik','Yildiz','Aydin','Ozturk',
                                  'Arslan','Dogan','Kilic','Aslan'];
    ranks         TEXT[] := ARRAY['Polis Memuru','Kidemli Polis Memuru','Basspolis Memuru',
                                  'Komiser Yardimcisi','Komiser','Baskomiser'];
    -- 7 elemanli: birim sayilariyla (3 ve 6) ortak boleni yok, her birime her durumdan dusuyor
    statuses      TEXT[] := ARRAY['SAHADA','SAHADA','SAHADA','SAHADA','MERKEZDE','IZINDE','RAPORLU'];
    task_types    TEXT[] := ARRAY['DEVRIYE','RADAR','MOTOSIKLET','OKUL_GECIDI','KAZA_INCELEME'];
    locations     TEXT[] := ARRAY['D-100 Karayolu','Cevre Yolu 12. km','Merkez Kavsagi','Istasyon Caddesi',
                                  'Universite Kavsagi','Sanayi Bulvari','Sahil Yolu','Otogar Cikisi'];
    car_brands    TEXT[] := ARRAY['Fiat','Renault','Ford','Toyota','Volkswagen'];
    car_models    TEXT[] := ARRAY['Egea','Megane','Focus','Corolla','Passat'];
    moto_brands   TEXT[] := ARRAY['Honda','BMW','Yamaha'];
    moto_models   TEXT[] := ARRAY['NC750X','F850GS','Tracer 900'];

    city_record   RECORD;
    police_count  INTEGER;
    unit_count    INTEGER;
    unit_id_value VARCHAR(20);
    idx           INTEGER := 0;   -- tum sehirler boyunca artan sayac
    i             INTEGER;
    j             INTEGER;
    police_id     VARCHAR(20);
    police_task   VARCHAR(20);
    task_count    INTEGER;
    is_moto       BOOLEAN;
    task_status   VARCHAR(20);
BEGIN
    FOR city_record IN SELECT * FROM city ORDER BY sort_order LOOP

        police_count := CASE city_record.id
            WHEN '34' THEN 48 WHEN '06' THEN 36 WHEN '35' THEN 30 WHEN '16' THEN 21
            WHEN '07' THEN 24 WHEN '01' THEN 18 WHEN '42' THEN 15 WHEN '27' THEN 18
            WHEN '38' THEN 12 WHEN '55' THEN 12 WHEN '61' THEN  9 WHEN '21' THEN 15
            WHEN '25' THEN  9 WHEN '65' THEN  9 ELSE 12 END;

        SELECT count(*) INTO unit_count FROM unit WHERE city_id = city_record.id;

        FOR i IN 0..police_count - 1 LOOP

            SELECT id INTO unit_id_value
            FROM unit
            WHERE city_id = city_record.id AND seq = (i % unit_count) + 1;

            police_id   := city_record.plate_code || '-' || (1001 + i);
            police_task := task_types[(idx % 5) + 1];
            task_count  := 3 + (idx % 8);

            INSERT INTO police (id, badge_number, full_name, age, police_rank, score, photo_url, phone,
                                start_date, city_id, unit_id, status, task_type,
                                daily_task_count, daily_task_limit, vehicle_plate)
            VALUES (
                police_id,
                city_record.plate_code || lpad((1001 + i)::TEXT, 4, '0'),
                first_names[(idx % 16) + 1] || ' ' || last_names[((idx / 3) % 12) + 1],
                24 + (idx % 30),
                ranks[(idx % 6) + 1],
                60 + (idx % 41),
                '/images/police/' || police_id || '.jpg',
                '05' || lpad((300000000 + idx * 137)::TEXT, 9, '0'),
                make_date(2005 + (idx % 18), 3, 1),
                city_record.id,
                unit_id_value,
                statuses[(idx % 7) + 1],
                police_task,
                task_count,
                8,
                city_record.plate_code || ' TP ' || (100 + i)
            );

            -- arac
            is_moto := police_task = 'MOTOSIKLET';

            INSERT INTO vehicle (plate, police_id, brand, model, model_year, vehicle_type,
                                 kilometers, last_maintenance_date, photo_url)
            VALUES (
                city_record.plate_code || ' TP ' || (100 + i),
                police_id,
                CASE WHEN is_moto THEN moto_brands[(idx % 3) + 1] ELSE car_brands[(idx % 5) + 1] END,
                CASE WHEN is_moto THEN moto_models[(idx % 3) + 1] ELSE car_models[(idx % 5) + 1] END,
                2016 + (idx % 9),
                CASE WHEN is_moto THEN 'Motosiklet' ELSE 'Otomobil' END,
                15000 + (idx * 1350) % 240000,
                make_date(2026, 1 + (idx % 12), 10),
                '/images/vehicle/' || CASE WHEN is_moto THEN 'motosiklet' ELSE 'otomobil' END
                    || '-' || ((idx % 5) + 1) || '.jpg'
            );

            -- gunluk gorevler
            FOR j IN 0..task_count - 1 LOOP

                task_status := CASE
                    WHEN j < task_count - 2 THEN 'TAMAMLANDI'
                    WHEN j = task_count - 2 THEN 'DEVAM'
                    ELSE 'PLANLANDI' END;

                INSERT INTO task (id, police_id, city_id, unit_id, task_type, location,
                                  start_time, end_time, task_status)
                VALUES (
                    police_id || '-G' || (j + 1),
                    police_id,
                    city_record.id,
                    unit_id_value,
                    CASE WHEN j = 0 THEN police_task ELSE task_types[((idx + j) % 5) + 1] END,
                    locations[((idx + j) % 8) + 1],
                    lpad((8 + j)::TEXT, 2, '0') || ':00',
                    lpad((9 + j)::TEXT, 2, '0') || ':00',
                    task_status
                );

            END LOOP;

            idx := idx + 1;
        END LOOP;
    END LOOP;
END $$;
