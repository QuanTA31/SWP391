SELECT
    COUNT(*)
FROM
    Assets
WHERE
    asset_id IN /* assetIds */(1)
    AND asset_status IN /* invalidStatuses */('07', '11')
