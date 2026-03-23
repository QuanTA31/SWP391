SELECT
    a.*
FROM
    assets a
    INNER JOIN users u ON a.current_user_id = u.id
WHERE
    a.asset_type_id = /* typeId */'01'
    AND a.asset_status_id = '02'
    AND u.status_id IN ('02', '03')
ORDER BY
    a.created_at DESC
