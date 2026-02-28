SELECT
    /*%expand*/*
FROM asset_external_request_detail
WHERE id = /* id */1
FOR UPDATE NOWAIT