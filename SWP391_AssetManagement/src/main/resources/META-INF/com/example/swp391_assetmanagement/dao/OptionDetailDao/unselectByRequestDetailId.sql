update option_detail
set
    is_selected = false,
    approved_date = null,
    approver_by = null
where asset_external_request_detail_id = /* requestDetailId */0
