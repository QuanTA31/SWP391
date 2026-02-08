SELECT
    od.*,
    ud.name AS approver_name
FROM option_detail od
         LEFT JOIN users u
                   ON od.approver_by = u.id
         LEFT JOIN user_detail ud
                   ON ud.user_id = u.id
WHERE od.asset_external_request_detail_id = /* requestDetailId */0
/*%if isSelected != null */
  AND od.is_selected = /* isSelected */true
/*%end*/
ORDER BY od.created_at DESC
    LIMIT /* pageSize */10 OFFSET /* offset */0
