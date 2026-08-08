-- =====================================================================
-- V6 - Gercek fotograf adresleri
--
-- V3 icindeki '/images/police/...' ve '/images/vehicle/...' yollarinin
-- projede karsiligi yoktu, her istek 404 donuyor ve kirik gorsel cikiyordu.
-- Personel portreleri randomuser.me, arac fotograflari Wikimedia Commons.
-- Uretim deterministiktir: ayni kayit her zaman ayni fotografi alir.
-- =====================================================================

-- personel portreleri: ad kadin isimlerindense women, degilse men
WITH gendered AS (
    SELECT id,
           CASE WHEN split_part(full_name, ' ', 1) IN ('Elif', 'Zeynep', 'Ayse', 'Fatma', 'Merve', 'Selin')
                THEN 'women'
                ELSE 'men'
           END AS gender
    FROM police
),
numbered AS (
    SELECT id,
           gender,
           (row_number() OVER (PARTITION BY gender ORDER BY id) - 1) % 100 AS photo_index
    FROM gendered
)
UPDATE police p
SET photo_url = 'https://randomuser.me/api/portraits/' || n.gender || '/' || n.photo_index || '.jpg'
FROM numbered n
WHERE p.id = n.id;

-- arac fotograflari: tipe gore 5'er gercek fotograf arasinda dagitilir
WITH numbered AS (
    SELECT plate,
           vehicle_type,
           (row_number() OVER (PARTITION BY vehicle_type ORDER BY plate) - 1) % 5 AS photo_index
    FROM vehicle
)
UPDATE vehicle v
SET photo_url = CASE
        WHEN v.vehicle_type = 'Motosiklet' THEN (ARRAY[
            'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Police_motorcycle_in_Istanbul_Turkey_01.JPG/960px-Police_motorcycle_in_Istanbul_Turkey_01.JPG',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Police_motorcycle_in_Istanbul_Turkey_02.JPG/960px-Police_motorcycle_in_Istanbul_Turkey_02.JPG',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Police_motorcycle_in_Istanbul_Turkey_03.JPG/960px-Police_motorcycle_in_Istanbul_Turkey_03.JPG',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Police_motorcycle_of_Turkey.jpg/960px-Police_motorcycle_of_Turkey.jpg',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Polis_Honda_XL1000V_Varadero%2C_Turkey.jpg/960px-Polis_Honda_XL1000V_Varadero%2C_Turkey.jpg'
        ])[n.photo_index + 1]
        ELSE (ARRAY[
            'https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Police_car_from_Turkey.jpg/960px-Police_car_from_Turkey.jpg',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Police_car_in_Turkey.JPG/960px-Police_car_in_Turkey.JPG',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Police_car_in_Turkey_02.JPG/960px-Police_car_in_Turkey_02.JPG',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Police_car_of_Turkey_01.jpg/960px-Police_car_of_Turkey_01.jpg',
            'https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Police_car_of_Turkey_02.jpg/960px-Police_car_of_Turkey_02.jpg'
        ])[n.photo_index + 1]
    END
FROM numbered n
WHERE v.plate = n.plate;
