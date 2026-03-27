SELECT a.asset_code,
       a.description,
       a.received_date,
       a.location_id,
       a.asset_status_id,
       a.current_user_id,
       a.asset_type_id,
       u.username,
       ud.name,
       u.status_id,
       COUNT(1) OVER() AS total_items
FROM assets AS a
LEFT JOIN users AS u ON u.id = a.current_user_id
LEFT JOIN user_detail AS ud ON u.id = ud.user_id
WHERE
    a.location_id != '05'
  AND a.asset_status_id IN ('01','02')
  /*%if @isNotEmpty(request.locationId) */
  AND a.location_id = /* request.locationId */'01'
  /*%end*/
  /*%if @isNotEmpty(request.assetTypeId) */
  AND a.asset_type_id = /* request.assetTypeId */'01'
  /*%end*/
  /*%if @isNotEmpty(request.assetCode) */
  AND Lower(a.asset_code) LIKE Lower(CONCAT('%', /* request.assetCode */'lap', '%'))
  /*%end*/
ORDER BY a.received_date DESC
LIMIT /* request.pageSize */15
OFFSET /* request.offset */0