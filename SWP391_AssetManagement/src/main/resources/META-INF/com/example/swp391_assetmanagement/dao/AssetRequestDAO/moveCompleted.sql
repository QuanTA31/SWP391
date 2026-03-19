UPDATE asset_request
SET request_status_id = /* completed */'06'
WHERE id = /* id */1
AND request_status_id = /* inProgress */'05'
