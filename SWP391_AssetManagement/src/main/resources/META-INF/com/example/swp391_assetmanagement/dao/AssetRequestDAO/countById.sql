SELECT COUNT(1)
FROM asset_request
INNER JOIN asset_external_request_detail ON asset_request.id = asset_external_request_detail.asset_request_id
WHERE asset_request.id = /* assetRequestId */1
AND asset_request.request_status_id != /* status */''
