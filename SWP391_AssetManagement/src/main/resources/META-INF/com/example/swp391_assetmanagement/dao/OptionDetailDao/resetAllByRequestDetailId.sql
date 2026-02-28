update option_detail
set
    is_selected = false,
    approved_date = /* approvedDate */null,
    approver_by = /* approverBy */0
where asset_external_request_detail_id = /* requestDetailId */0