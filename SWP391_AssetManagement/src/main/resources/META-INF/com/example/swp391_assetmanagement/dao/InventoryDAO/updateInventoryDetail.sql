UPDATE asset_internal_request_detail
SET
    is_done = /* entity.isDone */true,
    note = /* entity.note */'Confirmed'
WHERE
    id = /* entity.id */1