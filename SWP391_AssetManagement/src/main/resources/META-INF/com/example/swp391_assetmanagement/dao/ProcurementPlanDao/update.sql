update option_detail
set
    asset_external_process_id = /* optionDetail.assetExternalProcessId */0,
    unit_price = /* optionDetail.unitPrice */0,
    description = /* optionDetail.description */'',
    approval_status_id = /* optionDetail.approvalStatusId */0,
    merchant = /* optionDetail.merchant */'',
    approver_by = /* optionDetail.approverBy */0,
    approved_at = /* optionDetail.approvedAt */null
where
    id = /* optionDetail.id */0
