select od.*,
       ud.name as approver_name
from option_detail od
         left join users u on u.id = od.approver_by
         left join user_detail ud on ud.user_id = u.id
where od.asset_external_request_detail_id = /* request.requestDetailId */0
/*%if request.isSelected != null */
  and od.is_selected = /* request.isSelected */false
/*%end */
order by od.created_at desc, od.id desc
limit /* request.pageSize */0
offset /* request.offset */0
