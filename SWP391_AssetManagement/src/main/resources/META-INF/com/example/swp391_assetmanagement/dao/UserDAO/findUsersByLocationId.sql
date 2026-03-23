SELECT u.id, u.user_code, ud.name
FROM users u
JOIN user_detail ud ON u.id = ud.user_id
WHERE ud.location_id = /* locationId */'01'
  AND u.status_id = '01'
