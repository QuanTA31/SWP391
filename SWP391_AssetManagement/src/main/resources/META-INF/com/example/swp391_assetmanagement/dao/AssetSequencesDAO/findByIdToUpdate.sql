SELECT
    /*%expand*/*
FROM asset_sequences
WHERE asset_type = /* assetType */0
    FOR UPDATE NOWAIT
