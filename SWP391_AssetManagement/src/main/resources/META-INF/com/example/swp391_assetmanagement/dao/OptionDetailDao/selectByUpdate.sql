SELECT
    /*%expand*/*
FROM option_detail
WHERE asset_external_request_detail_id = /* assetExternalRequestDetailId */0
    FOR UPDATE NOWAIT
