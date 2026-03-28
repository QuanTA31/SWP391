UPDATE asset_request
SET request_status_id = /* completed */'06',
    handover_date = CURRENT_TIMESTAMP
WHERE id = /* id */1
AND request_status_id = /* research_done */'08'
