SELECT ud.user_code, /*sai o day ch nghi duoc*/
       u.username,
       u.password,
       u.role_id,
       u.user_status,
       u.user_location_id,
       ud.name,
       ud.phone,
       ud.email,
       COUNT(1) OVER() AS total_items
FROM users as u
inner join user_detail as ud into u.id = ud.user_id

WHERE 1 = 1
/*%if request.locationId != null && request.locationId != "" */
  AND location_id = /* request.locationId */0
/*%end */
/*%if request.roleID != null && request.roleID != "" */
  AND role_id = /* request.roleID */''
/*%end */
/*%if request.name != null && request.name != "" */
  AND name = /* request.name */''
/*%end */

    LIMIT /* request.pageSize */0
OFFSET /* request.offset */0