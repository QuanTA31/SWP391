SELECT a.id,
    a.location_id,
       a.asset_type_id
FROM assets a
WHERE a.id IN /* assetId */()