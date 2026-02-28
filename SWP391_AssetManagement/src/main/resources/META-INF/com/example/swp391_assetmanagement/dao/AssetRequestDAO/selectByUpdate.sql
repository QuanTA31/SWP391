SELECT
    /*%expand*/*
FROM asset_request
WHERE id = /* assetRequestId */0
    FOR UPDATE NOWAIT
