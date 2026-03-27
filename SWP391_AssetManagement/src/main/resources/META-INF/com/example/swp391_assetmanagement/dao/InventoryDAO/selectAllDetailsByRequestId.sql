SELECT
    asset_id,
    is_done,
    note
FROM asset_internal_request_detail
WHERE asset_request_id = /* request.requestId */1
