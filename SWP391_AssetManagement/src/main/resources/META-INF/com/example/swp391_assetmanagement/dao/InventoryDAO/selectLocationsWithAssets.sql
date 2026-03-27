SELECT l.id
FROM location l
WHERE l.id = '05'
UNION
SELECT DISTINCT l.id
FROM location l
JOIN assets a ON l.id = a.location_id
WHERE a.asset_status_id = '02'