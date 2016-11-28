nbApp.selectOptions = {
    country: {
        url: 'p?id=get-countries'
    },
    district: {
        url: 'p?id=get-districts',
        data: ['region']
    },
    region: {
        url: 'p?id=get-regions'
    },
    regiontype: {
        url: 'p?id=get-region-types'
    },
    locality: {
        url: 'p?id=get-localities'
    },
    localitytype: {
        url: 'p?id=get-locality-types'
    },
    street: {
        url: 'p?id=get-streets'
    },
    category: {
        url: 'p?id=get-document-type-category',
        minimumInputLength: 1,
        tags:true,
        minimumResultsForSearch:1
    }
};
