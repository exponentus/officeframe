nbApp.selectOptions = {
    organization: {
        url: 'p?id=get-organizations',
        onSelect: function(e) {
            if(e.params.data != undefined){
                $("select[name='department'], select[name='organization']").prop('required', false);
                $("select[name='department'], select[name='organization']").removeClass("required");
                $("select[name='department'], select[name='organization']").closest(".controls").removeClass("has-error")
            }
        }
    },
    orgcategory: {
        url: 'p?id=get-org-categories'
    },
    department: {
        url: 'p?id=get-departments',
        data: ['organization'],
    onSelect: function(e) {
        if(e.params.data != undefined) {
            $("select[name='department'], select[name='organization']").prop('required', false);
            $("select[name='department'], select[name='organization']").removeClass("required");
            $("select[name='department'], select[name='organization']").closest(".controls").removeClass("has-error")
        }
        
    }
    },
    position: {
        url: 'p?id=get-positions'
    },
    departmenttype: {
        url: 'p?id=get-departmenttype'
    }
};
