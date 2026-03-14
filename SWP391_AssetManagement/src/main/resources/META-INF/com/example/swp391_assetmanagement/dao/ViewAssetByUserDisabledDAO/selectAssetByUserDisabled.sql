SELECT a.asset_code,
       a.description,
       a.received_date,
       a.location_id,
       a.asset_status_id,
       a.current_user_id,
       a.asset_type_id,
       u.username,
       u.name,
       u.status_id,
       COUNT(1) OVER() AS total_items
FROM users AS u
INNER JOIN assets AS a ON u.id = a.current_user_id
WHERE u.status_id = /* request.userStatus */'03'
  /*%if request.locationId != null */
  AND a.location_id = /* request.locationId */'L01'
  /*%end*/
  /*%if request.assetTypeId != null */
  AND a.asset_type_id = /* request.assetTypeId */'T01'
  /*%end*/
  /*%if request.name != null */
  AND Lower(u.name) LIKE Lower (/* request.name */'%a%')
  /*%end*/
ORDER BY a.received_date DESC
LIMIT /* request.pageSize */15
OFFSET /* request.offset */0