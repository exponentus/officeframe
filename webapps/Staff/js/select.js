nbApp.selectOptions = {
    organization: {
        url: 'p?id=get-organizations'
    },
    orgcategory: {
        url: 'p?id=get-org-categories'
    },
    department: {
        url: 'p?id=get-departments',
        data: ['organization']
    },
    position: {
        url: 'p?id=get-positions'
    },
    departmenttype: {
        url: 'p?id=get-departmenttype'
    }
};
