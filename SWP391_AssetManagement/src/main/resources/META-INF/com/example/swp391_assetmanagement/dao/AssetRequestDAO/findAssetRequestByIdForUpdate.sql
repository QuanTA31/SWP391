SELECT
    /*%expand*/*
FROM asset_request
WHERE asset_request.id = /* id */1
    FOR UPDATE NOWAIT
