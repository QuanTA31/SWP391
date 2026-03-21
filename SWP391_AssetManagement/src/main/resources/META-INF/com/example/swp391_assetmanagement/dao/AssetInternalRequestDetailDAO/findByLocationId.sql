SELECT
    a.id,
    a.asset_code,
    a.asset_type_id,
    a.description,
    a.asset_status_id
FROM assets a
WHERE a.location_id = /* locationId */''
ORDER BY a.asset_code
