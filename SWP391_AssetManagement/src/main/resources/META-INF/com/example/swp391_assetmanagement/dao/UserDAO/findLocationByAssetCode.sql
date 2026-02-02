SELECT location_id
FROM users AS u
         INNER JOIN user_detail AS ud ON u.id = ud.user_id
WHERE u.user_code = /* asserCode */''
