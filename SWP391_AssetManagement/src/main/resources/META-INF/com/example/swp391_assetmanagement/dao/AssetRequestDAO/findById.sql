SELECT
    /*%expand*/*
FROM asset_request
WHERE id = /* id */1
FOR UPDATE NOWAIT