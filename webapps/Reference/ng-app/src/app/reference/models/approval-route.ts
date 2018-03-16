export class ApprovalRoute {
    id: string = '';
    kind: string = 'approvalRoute';
    editable: boolean;
    //
    name: string;
    on: boolean;
    schema: 'UNKNOWN' | 'REJECT_IF_NO' | 'IN_ANY_CASE_DECIDE_SIGNER' | 'WITHOUT_APPROVAL';
    category: string;
    locName: { [langCode: string]: string };
    routeBlocks: RouteBlock[];
}

export class RouteBlock {
    id: string = '';
    tid: string = '' + Date.now();
    isNew: boolean = true;
    //
    position: number;
    type: 'UNKNOWN' | 'SERIAL' | 'PARALLEL' | 'SIGNING' = 'UNKNOWN';
    timeLimit: number = 0;
    requireCommentIfNo: boolean = false;
    approvers: any[] = [];
}
