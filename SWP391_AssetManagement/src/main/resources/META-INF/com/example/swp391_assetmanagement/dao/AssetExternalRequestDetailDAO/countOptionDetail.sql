SELECT COUNT(1)
FROM asset_external_request_detail a
LEFT JOIN option_detail o
    ON a.id = o.asset_external_request_detail_id
WHERE o.id IS NULL
AND a.asset_request_id = /* assetRequestId */0