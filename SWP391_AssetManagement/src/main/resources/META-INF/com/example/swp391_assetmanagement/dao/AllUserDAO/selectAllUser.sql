SELECT u.username,
       u.password,
       u.role_id,
       u.status_id,
       ud.location_id,
       ud.name,
       ud.phone,
       ud.email,
       COUNT(1) OVER() AS total_user
FROM users AS u
INNER JOIN user_detail AS ud ON u.id = ud.user_id

WHERE 1 = 1
/*%if request.locationId != null && request.locationId != "" */
  AND location_id = /* request.locationId */0
/*%end */
/*%if request.roleID != null && request.roleID != "" */
  AND role_id = /* request.roleID */''
/*%end */
/*%if request.userStatus != null && request.userStatus != "" */
  AND status_id = /* request.userStatus */''
/*%end */
/*%if request.name != null && request.name != "" */
  AND Lower(ud.name) LIKE Lower(CONCAT('%', /* request.name */'lap', '%'))
/*%end */

LIMIT /* request.pageSize */0
OFFSET /* request.offset */0