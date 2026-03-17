<<<<<<< HEAD
SELECT a.id,
    a.location_id,
       a.asset_type_id
FROM assets a
WHERE a.id IN /* assetId */()
=======
SELECT
/*%expand*/*
FROM assets
WHERE id = /* id */0
>>>>>>> main
