SELECT COUNT(*)
FROM asset_external_request_detail
WHERE asset_request_id = /* requestId */0
AND external_status_id <> 3