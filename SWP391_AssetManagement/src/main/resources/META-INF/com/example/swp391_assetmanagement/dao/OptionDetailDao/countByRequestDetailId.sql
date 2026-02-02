select count(1)
from option_detail
where asset_external_request_detail_id = /* request.requestDetailId */0
/*%if request.isSelected != null */
  and is_selected = /* request.isSelected */false
/*%end */
