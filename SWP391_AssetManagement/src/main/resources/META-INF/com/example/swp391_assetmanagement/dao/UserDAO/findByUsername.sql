select ud.name,
       u.role_id
from users as u join user_detail as ud on ud.user_id = u.id
where username = /* userDAORequest.username */''
and password = /* userDAORequest.password */''