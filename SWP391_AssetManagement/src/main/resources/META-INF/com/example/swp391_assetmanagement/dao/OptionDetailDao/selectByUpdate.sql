SELECT
    /*%expand*/*
FROM option_detail
WHERE asset_external_request_detail_id = /* assetExternalRequestDetailId */0
  AND (SELECT COUNT(1)
       FROM option_detail
       WHERE asset_external_request_detail_id = /* assetExternalRequestDetailId */0)
    = (SELECT COUNT(1)
       FROM option_detail
       WHERE asset_external_request_detail_id = /* assetExternalRequestDetailId */0
         AND is_selected IS NULL)
    FOR UPDATE NOWAIT
