SELECT
    /*%expand*/*
FROM asset_request
WHERE asset_request.id = /* assetRequestId */1
    FOR UPDATE NOWAIT
