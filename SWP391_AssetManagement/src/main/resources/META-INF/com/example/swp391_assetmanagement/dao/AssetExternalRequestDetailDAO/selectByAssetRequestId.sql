SELECT
    id,
    asset_request_id,
    asset_type_id,
    external_status_id,
    quantity,
    note,
    created_at
FROM
    asset_external_request_detail
WHERE
    asset_request_id = /* assetRequestId */0