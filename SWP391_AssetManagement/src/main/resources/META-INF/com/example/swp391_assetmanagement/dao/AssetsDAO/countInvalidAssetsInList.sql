SELECT
    COUNT(*)
FROM
    Assets
WHERE
    id IN /* assetIds */(1)
    AND asset_status_id IN /* invalidStatuses */('07', '11')
