SELECT
/*%expand*/*
FROM assets
WHERE asset_status_id = /* status */''
    FOR UPDATE NOWAIT
