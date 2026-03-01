SELECT asset_request.id
FROM asset_request
         INNER JOIN asset_external_request_detail ON asset_request.id = asset_external_request_detail.asset_request_id
WHERE asset_external_request_detail.id = /* assetRequestDetailId */1
