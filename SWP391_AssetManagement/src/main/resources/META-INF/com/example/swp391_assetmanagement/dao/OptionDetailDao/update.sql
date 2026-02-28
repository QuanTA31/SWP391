update option_detail
set
    is_selected = /* optionDetail.isSelected */false,
    approved_date = /* optionDetail.approvedDate */null,
    approver_by = /* optionDetail.approverBy */null
where
    id = /* optionDetail.id */0
