SELECT
    /*%expand*/*
FROM asset_external_request_detail
WHERE asset_request_id = /* assetRequestId */0
    FOR UPDATE NOWAIT
