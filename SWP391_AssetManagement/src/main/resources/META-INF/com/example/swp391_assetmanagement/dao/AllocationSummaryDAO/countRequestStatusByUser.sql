SELECT
    ar.request_status_id,
    COUNT(*) AS count
FROM
    asset_request ar
WHERE
    ar.requested_by = /* userId */1
GROUP BY
    ar.request_status_id;
