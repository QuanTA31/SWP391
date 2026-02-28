SELECT
    asset_request.id
FROM asset_external_request_detail
INNER JOIN asset_request ON asset_request.id = asset_external_request_detail.asset_request_id
WHERE asset_external_request_detail.id = /* assetRequestDetailId */0