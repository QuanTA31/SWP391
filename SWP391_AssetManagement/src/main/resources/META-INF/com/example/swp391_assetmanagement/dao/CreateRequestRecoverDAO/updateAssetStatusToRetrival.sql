UPDATE assets
SET
    asset_status_id = '09'
WHERE
    id IN /* assetIds */(1, 2, 3)
