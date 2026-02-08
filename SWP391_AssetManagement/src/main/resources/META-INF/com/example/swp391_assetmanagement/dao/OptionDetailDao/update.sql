update option_detail
set
    asset_external_request_detail_id = /* optionDetail.assetExternalRequestDetailId */0,
    unit_price = /* optionDetail.unitPrice */0,
    `describe` = /* optionDetail.description */'',
    merchant = /* optionDetail.merchant */'',
    warranty_period = /* optionDetail.warrantyPeriod */null,
    is_selected = /* optionDetail.isSelected */false,
    approved_date = /* optionDetail.approvedDate */null,
    approver_by = /* optionDetail.approverBy */null
where
    id = /* optionDetail.id */0
