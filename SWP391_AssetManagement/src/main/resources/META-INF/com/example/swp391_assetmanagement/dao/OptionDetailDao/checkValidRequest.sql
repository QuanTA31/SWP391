SELECT
    CASE
        WHEN COUNT(*) = (
            SELECT COUNT(*)
            FROM asset_external_request_detail
            WHERE id != /* requestDetailId */0
              AND asset_request_id = /* assetRequestId */0
        ) THEN TRUE
        ELSE FALSE
    END as status
FROM (
    SELECT ard.id
    FROM asset_external_request_detail ard
    JOIN option_detail od
        ON ard.id = od.asset_external_request_detail_id
    WHERE ard.asset_request_id = /* assetRequestId */0
      AND ard.id != /* requestDetailId */0
    GROUP BY ard.id
    HAVING SUM(CASE WHEN od.is_selected = 1 THEN 1 ELSE 0 END) = 1
) as valid_details;