SELECT count(*)
FROM asset_internal_request_detail
WHERE asset_request_id = /* requestId */1
  AND is_done = false