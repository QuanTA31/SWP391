UPDATE option_detail
SET is_selected = /* details.isSelected */0,
    approved_date = /* details.approvedDate */null,
    approver_by = /* details.approverBy */null
WHERE id = /* details.id */1
