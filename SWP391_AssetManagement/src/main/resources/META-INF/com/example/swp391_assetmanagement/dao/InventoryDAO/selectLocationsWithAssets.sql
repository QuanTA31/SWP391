SELECT DISTINCT l.id
FROM location l
JOIN assets a ON l.id = a.location_id
WHERE a.asset_status_id = '02'